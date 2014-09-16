package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.ActivityReport;

public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long>, JpaSpecificationExecutor<ActivityReport>{

}
