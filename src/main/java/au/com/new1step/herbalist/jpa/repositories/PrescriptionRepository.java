package au.com.new1step.herbalist.jpa.repositories;


import au.com.new1step.herbalist.common.ProcessingStatus;
import au.com.new1step.herbalist.jpa.model.Doctor;
import au.com.new1step.herbalist.jpa.model.Patient;
import au.com.new1step.herbalist.jpa.model.Prescription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    public Prescription findByPsID(Long id);
    public List<Prescription> findByProcessingStatus(ProcessingStatus processingStatus);
    public List<Prescription> findFirst3ByProcessingStatusOrderByUpdateDate(ProcessingStatus processingStatus);
    public Prescription findFirstByProcessingStatusOrderByUpdateDateAsc(ProcessingStatus processingStatus);
    public Prescription findFirstByProcessingStatusOrderByUpdateDateDesc(ProcessingStatus processingStatus);

    public Prescription findFirstByPatientOrderByUpdateDateDesc(Patient patient);

    public List<Prescription> findByPatientOrderByUpdateDateDesc(Patient patient,Pageable pageRequest);

    @Query("select count(p) from Prescription p where p.doctor = :doctor and p.updateDate between :todayMidnight and :tomorrowMidnight")
    public Integer findDailyNoOfPatients(@Param("doctor") Doctor doctor, @Param("todayMidnight") LocalDateTime todayMidnight, @Param("tomorrowMidnight") LocalDateTime tomorrowMidnight);

    @Query("select sum(p.fee) from Prescription p where p.doctor = :doctor and p.updateDate between :todayMidnight and :tomorrowMidnight")
    public Double findDailyServiceFee(@Param("doctor") Doctor doctor, @Param("todayMidnight") LocalDateTime todayMidnight, @Param("tomorrowMidnight") LocalDateTime tomorrowMidnight);

    // time between 01/05/2018:00:00:00 and 31/05/2018:11:59:59
    @Query("select count(p) from Prescription p where p.doctor = :doctor and p.updateDate between :dayOneMidnight and :lastDayMidnight")
    public Integer findMonthlyNoOfPatients(@Param("doctor") Doctor doctor, @Param("dayOneMidnight") LocalDateTime dayOneMidnight, @Param("lastDayMidnight") LocalDateTime lastDayMidnight);

    @Query("select sum(p.fee) from Prescription p where p.doctor = :doctor and p.updateDate between :dayOneMidnight and :lastDayMidnight")
    public Double findMonthlyServiceFee(@Param("doctor") Doctor doctor, @Param("dayOneMidnight") LocalDateTime dayOneMidnight, @Param("lastDayMidnight") LocalDateTime lastDayMidnight);

    @Query(" select p from Prescription p where p.doctor = :doctor and p.updateDate between :firstDayOfYear and :lastDayOfYear")
    public List<Prescription> findPrescriptionsByYear(@Param("doctor") Doctor doctor, @Param("firstDayOfYear") LocalDateTime firstDayOfYear, @Param("lastDayOfYear") LocalDateTime lastDayOfYear);

}

