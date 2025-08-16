package com.joca.services;

import com.joca.database.activity.ActivityDB;
import com.joca.database.attendance.AttendanceDB;
import com.joca.model.activity.Activity;
import com.joca.model.attendance.Attendance;
import com.joca.model.exceptions.DuplicatedKeyException;
import com.joca.model.exceptions.InvalidRequisitesException;
import com.joca.model.exceptions.NotFoundException;
import com.joca.model.exceptions.NotRowsAffectedException;
import com.joca.model.filter.Filter;
import com.joca.model.filter.FilterTypeEnum;

import java.sql.SQLException;
import java.util.List;

public class AttendanceService {
    private AttendanceDB attendanceDB;

    public AttendanceService(AttendanceDB attendanceDB) {
        this.attendanceDB = attendanceDB;
    }

    public void registerAttendance(Attendance attendance) throws SQLException, DuplicatedKeyException, NotRowsAffectedException, NotFoundException, InvalidRequisitesException {
        if (isKeysInUse(attendance.getParticipantEmail(), attendance.getActivityId())) {
            throw new DuplicatedKeyException("Error al registrar la asistencia, el participante : "
                    + attendance.getParticipantEmail() + " ya registro su asistencia en la actividad: " + attendance.getActivityId());
        }
        if (!isAvailableCapacity(attendance.getActivityId())) {
            throw new InvalidRequisitesException("Error al registrar la asistencia, la actividad ya alcanzó su cupo máximo");
        }
        attendanceDB.insert(attendance);
    }

    public Attendance getAttendanceByID(String participantEmail, String activityId) throws SQLException, NotFoundException, InvalidRequisitesException {
        Filter filterEmail = new Filter("participant_email", participantEmail, FilterTypeEnum.EQUAL);
        Filter filterEvent = new Filter("activity_id", activityId, FilterTypeEnum.EQUAL);
        List<Attendance> attendances = attendanceDB.findByAttributes(List.of(filterEmail, filterEvent));
        return attendances.get(0);
    }

    public List<Attendance> getAllAttendances() throws SQLException, NotFoundException {
        return attendanceDB.findAll();
    }

    public void updateAttendance(Attendance attendance, String participantEmail, String activityId) throws SQLException, DuplicatedKeyException, NotRowsAffectedException {
        if (isKeysInUse(attendance.getParticipantEmail(), attendance.getActivityId())
                && !isSameKey(attendance, participantEmail, activityId)) {
            throw new DuplicatedKeyException("Error al actualizar la asistencia, el participante : "
                    + attendance.getParticipantEmail() + " ya registro su asistencia en la actividad: " + attendance.getActivityId());
        }

        attendanceDB.updateByKeys(attendance, participantEmail, activityId);
    }

    public void deleteAttendance(String participantEmail, String activityId) throws SQLException, NotRowsAffectedException {
        attendanceDB.deleteByKeys(participantEmail, activityId);
    }

    public boolean isKeysInUse(String participantEmail, String activityId) throws SQLException {
        return attendanceDB.isKeysInUse(participantEmail, activityId);
    }

    public List<Attendance> getAttendancesByFilter(List<Filter> filters) throws SQLException, NotFoundException, InvalidRequisitesException {
        return attendanceDB.findByAttributes(filters);
    }

    private boolean isSameKey(Attendance attendance, String oldEmail, String oldId) {
        return attendance.getParticipantEmail().equals(oldEmail) && attendance.getActivityId().equals(oldId);
    }

    public boolean isAvailableCapacity(String activityId) throws SQLException, NotFoundException {
        int actualCapacity = getActualAttendances(activityId);
        ActivityDB activityDB = new ActivityDB();
        Activity activity = activityDB.findByKey(activityId);
        return activity.getMaxCapacity() > actualCapacity;
    }

    public int getActualAttendances(String activityId) throws SQLException {
        return attendanceDB.getAttendancesQuantity(activityId);
    }

    public boolean canGenerateCertificate(String participantEmail, String eventId) throws SQLException {
        return attendanceDB.canGenerateCertificate(participantEmail, eventId);
    }
}
