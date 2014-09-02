package fr.wati.yacramanager.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact>{

}
