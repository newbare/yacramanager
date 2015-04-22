package fr.wati.yacramanager.dao;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import fr.wati.yacramanager.beans.CompanyTempInvitation;

public class JdbcCompanyInvitationRepository {

	private JdbcTemplate jdbcTemplate;
	
	private static final String insertSQL="insert into CompanyTempInvitation (userId,companyId,token,createdDate,expiryDate)"
			+ " values (?,?,?,?,?)"; 
	private static final String findInvitationWithTokenSQL="select userId,companyId,token,createdDate,expiryDate from CompanyTempInvitation where userId= :userId and companyId= :companyId and token= :token";
	private static final String findInvitationSQL="select userId,companyId,token,createdDate,expiryDate from CompanyTempInvitation where userId= :userId and companyId= :companyId";
	private static final String deleteSql = "DELETE FROM CompanyTempInvitation WHERE userId = ? and companyId = ?";
	
	public JdbcCompanyInvitationRepository(DataSource dataSource) {
		this.jdbcTemplate=new JdbcTemplate(dataSource);
	}

	public CompanyTempInvitation addInvitation(String userId, String companyId){
		String token=RandomStringUtils.randomAlphanumeric(30);
		// define query arguments
		Date expiryDate=new LocalDate().plusDays(3).toDate();
		Object[] params = new Object[] { userId, companyId, token, new Date(), expiryDate};
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE,Types.DATE}; 
		jdbcTemplate.update(insertSQL, params,types);
		return findInvitationWithToken(userId,companyId,token);
	}
	
	public CompanyTempInvitation findInvitationWithToken(String userId, String companyId,String token){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userId", userId);
		parameters.addValue("companyId", companyId);
		parameters.addValue("token", token);
		 List<CompanyTempInvitation> invitations = new NamedParameterJdbcTemplate(jdbcTemplate).query(findInvitationWithTokenSQL,parameters ,new BeanPropertyRowMapper<CompanyTempInvitation>(CompanyTempInvitation.class));
		 if(invitations!=null && !invitations.isEmpty()){
			 return invitations.get(0);
		 }
		 return null;
	}
	
	public CompanyTempInvitation findInvitation(String userId, String companyId){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userId", userId);
		parameters.addValue("companyId", companyId);
		 List<CompanyTempInvitation> invitations = new NamedParameterJdbcTemplate(jdbcTemplate).query(findInvitationSQL,parameters ,new BeanPropertyRowMapper<CompanyTempInvitation>(CompanyTempInvitation.class));
		 if(invitations!=null && !invitations.isEmpty()){
			 return invitations.get(0);
		 }
		 return null;
	}
	
	public void removeInvitation(String userId, String companyId){
		Object[] params = new Object[] { userId, companyId};
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR}; 
		jdbcTemplate.update(deleteSql, params,types);
	}
}
