package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Activities;

public interface ActivityRepository extends JpaRepository<Activities, Long> {

}
