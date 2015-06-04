package fr.wati.yacramanager.services;

import fr.wati.yacramanager.web.dto.SearchResult;

public interface GlobalSearchService {

	SearchResult search(String textTosearch);
}
