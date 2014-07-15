package fr.wati.yacramanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
