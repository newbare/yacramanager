package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import fr.wati.yacramanager.beans.Activities;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.dao.repository.ActivitiesRepository;
import fr.wati.yacramanager.services.ActivityService;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO.ActivityDTO;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO.ActivityDay;
import fr.wati.yacramanager.web.dto.ActivitiesDetailsDTO.ActivityItem;

@Service
public class ActivityServiceImpl implements ActivityService {

	@Inject
	private ActivitiesRepository activitiesRepository;
	
	@Inject
	private DtoMapper dtoMapper;
	
	public ActivityServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Activities> findForUser(Long userId, Pageable pageable) {
		return activitiesRepository.findByUser_Id(userId, pageable);
	}

	@Override
	public List<Activities> findForCompany(Long comapanyId, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Activities> findForProject(Long projectId, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Activities> findForClient(Long clientId, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public ActivitiesDetailsDTO fromActivities(List<Activities> activities) {
		ActivitiesDetailsDTO activitiesDetailsDTO=new ActivitiesDetailsDTO();
		Multimap<LocalDate, Activities> multimap = Multimaps.index(
				activities,
				new Function<Activities, LocalDate>() {
					@Override
					public LocalDate apply(Activities activities) {
						return activities.getDate().toLocalDate();
					}
				});
		List<ActivityDay> activityDays=new ArrayList<>();
		for(Entry<LocalDate,Collection<Activities>> entry:multimap.asMap().entrySet()){
			ActivityDay activityDay=new ActivityDay();
			List<ActivityItem> activityItems=new ArrayList<>();
			for(Activities currentActivities:entry.getValue()){
				ActivityItem activityItem=new ActivityItem();
				ActivityDTO activityDTO=new ActivityDTO();
				activityDTO.setActivityOperation(currentActivities.getActivityOperation());
				activityDTO.setDate(currentActivities.getDate());
				activityDTO.setEntityId(currentActivities.getEntityId());
				activityDTO.setEntityType(currentActivities.getEntityType());
				activityDTO.setEmploye(dtoMapper.map((Employe)currentActivities.getUser()));
				activityItem.setTime(currentActivities.getDate().toLocalTime());
				activityItem.setActivityDTO(activityDTO);
				activityItems.add(activityItem);
			}
			activityDay.setDate(entry.getKey());
			activityDay.setActivityItems(activityItems);
			activityDays.add(activityDay);
		}
		activitiesDetailsDTO.setDays(activityDays);
		return activitiesDetailsDTO;
	}

}
