package fr.wati.yacramanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
