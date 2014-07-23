angular.module('ngHtmlCompile', []).directive(
		'ngHtmlCompile',
		function($compile) {
			return {
				restrict : 'A',
				link : function(scope, element, attrs) {
					scope.$watch(attrs.ngHtmlCompile, function(newValue,
							oldValue) {
						element.html(newValue);
						$compile(element.contents())(scope);
					});
				}
			}
		});
angular
		.module('ng-criterias', [ 'ng' ])
		.directive(
				'ngCriteria',
				[
						'$sce',
						'$timeout',
						'$templateCache',
						'$q',
						'$http',
						'$compile',
						'$filter',
						function($sce, $timeout, $templateCache, $q, $http,
								$compile, $filter) {
							return {
								restrict : 'AE',
								replace : true,
								scope : {
									// models
									criteriaConfig : '=',

									// callbacks
									onClose : '&',
									onOpen : '&',
									onFilterTriggered : '&',
									onRemoveCriteria : '&'
								},
								template : '<span class="ng-criteria inlineBlock" data-ng-show="criteriaConfig.displayed">'
										+ '<div class="btn-group">'
										+ '<button type="button" class="btn btn-default criteria-btn" ng-click="toggleFilterContent( $event )" ng-bind-html="buttonLabel"></button>'
										+ '<button type="button" class="btn btn-default" data-ng-click="dismissCriteria(criteriaConfig.name)" data-ng-show="closeable">'
										+ '<span aria-hidden="true">&times;</span>'
										+ '</button>'
										+ '</div>'
										+'<div class="filter-content hide col-md-2 col-xs-3" ng-html-compile="filterContentHTML">'
										+'</div>' + '</span>',
								link : function($scope, element, attrs) {
									// default config values
									var textFilterTemplate = _contextPath
											+ '/assets/others/criteria/criteria-text.tpl.html';
									var checkBoxFilterTemplate = _contextPath
											+ '/assets/others/criteria/criteria-checkbox.tpl.html';
									var dateFilterTemplate = _contextPath
											+ '/assets/others/criteria/criteria-date.tpl.html';
									var booleanFilterTemplate = _contextPath
											+ '/assets/others/criteria/criteria-boolean.tpl.html';

									if ($scope.criteriaConfig.closeable !== undefined) {
										$scope.closeable = $scope.criteriaConfig.closeable;
									} else {
										$scope.closeable = true;
									}
									$scope.dismissCriteria = function(fieldName) {
										$scope.criteriaConfig.displayed=false;
										$scope.onRemoveCriteria(fieldName);
										console.log("Remove criteria: "+fieldName);
									};
									/*
									 * Common config for all type
									 */
									$scope.filterValue = $scope.criteriaConfig.filterValue;
									$scope.computeButtonLabel = function(value) {
										$scope.buttonLabel = $sce
												.trustAsHtml($scope.criteriaConfig.defaultButtonLabel
														+ ': '
														+ value
														+ ' <span class="caret"></span>');
									}
									$scope.resetButtonLabel = function() {
										$scope.buttonLabel = $sce
												.trustAsHtml($scope.criteriaConfig.defaultButtonLabel
														+ ' <span class="caret"></span>');
									};
									$scope.resetButtonLabel();

									$scope.filterType = $scope.criteriaConfig.filterType;
									$scope.filterContentHTML=undefined;

									$scope.onFilterText = function() {
										var filter = {
											type  : "TEXT",
											field : $scope.criteriaConfig.name,
											value : $scope.filterValue
										};
										if ($scope.filterValue !== "") {
											$scope.computeButtonLabel('('
													+ $scope.filterValue + ')')
										} else {
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										$scope.criteriaConfig.onFilter(filter);
										$scope.criteriaConfig.currentFilter=filter;
										$scope.onFilterTriggered(filter);
									};

									$scope.onFilterDate = function() {
										var filter = {
											type  : "DATE",
											field : $scope.criteriaConfig.name,
											value : {
												start : $scope.startDate,
												end : $scope.endDate
											}
										};
										if ($scope.startDate !== undefined
												&& $scope.endDate !== undefined) {
											$scope.computeButtonLabel('('
													+ $filter('date')(
															$scope.startDate,
															'shortDate')
													+ '-'
													+ $filter('date')(
															$scope.endDate,
															'shortDate') + ')')
										} else {
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										$scope.criteriaConfig.onFilter(filter);
										$scope.criteriaConfig.currentFilter=filter;
										$scope.onFilterTriggered(filter);
									};
									$scope.onFilterBoolean = function() {
										var filter = {
											type  : "BOOLEAN",
											field : $scope.criteriaConfig.name,
											value : $scope.booleanValue
										};
										if ($scope.booleanValue !== undefined) {
											$scope
													.computeButtonLabel('('
															+ $scope.booleanValue
															+ ')')
										} else {
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										$scope.criteriaConfig.onFilter(filter);
										$scope.criteriaConfig.currentFilter=filter;
										$scope.onFilterTriggered(filter);
									};

									$scope.onFilterCheckBox = function() {
										var selected = [];
										var btnLabel = "";
										var i = 0;
										angular.forEach(
												$scope.checkboxElements,
												function(entry) {
													if (entry.ticked) {
														selected.push(entry);
														if (i > 0) {
															btnLabel = btnLabel
																	+ ' , ';
														}
														btnLabel = btnLabel
																+ entry.name;
														i++;
													}
												});
										if (btnLabel !== "") {
											$scope.computeButtonLabel(btnLabel)
										} else {
											$scope.resetButtonLabel();
										}
										var filter = {
											type  : "ARRAY",
											field : $scope.criteriaConfig.name,
											value : selected
										};
										$scope.criteriaConfig.onFilter(filter);
										$scope.criteriaConfig.currentFilter=filter;
										$scope.onFilterTriggered(filter);
									};
									function fetchTemplate(template) {
										return $q.when(	$templateCache.get(template)|| $http.get(template))
												.then(function(res) {
															if (angular.isObject(res)) {
																$templateCache.put(template,res.data);
																return res.data;
															}
															return res;
														});
									}
									$scope.openFilterContent = function() {
										angular
												.element(
														$scope.filterContentDiv)
												.addClass('show');
										angular
												.element(
														$scope.filterContentDiv)
												.removeClass('hide');
										angular.element(clickedEl).addClass(
												'buttonClicked');
										angular.element(document).bind('click',
												$scope.externalClickListener);
									};
									$scope.closeFilterContent = function() {
										angular
												.element(
														$scope.filterContentDiv)
												.removeClass('show');
										angular
												.element(
														$scope.filterContentDiv)
												.addClass('hide');
										angular.element(clickedEl).removeClass(
												'buttonClicked');
										angular.element(document).unbind(
												'click',
												$scope.externalClickListener);
									};
									// UI operations to show/hide checkboxes
									// based on click event..
									$scope.toggleFilterContent = function(e) {

										// We grab the checkboxLayer
										$scope.filterContentDiv = element
												.children()[1];

										// compute content depending on the
										// filter type
										if ($scope.filterType === "TEXT") {
											if($scope.filterContentHTML===undefined){
												fetchTemplate(textFilterTemplate)
												.then(
														function(content) {
															$scope.filterContentHTML = content;
														});
											}
															
										} else if ($scope.filterType === "ARRAY") {
											if($scope.filterContentHTML===undefined){
												fetchTemplate(
													checkBoxFilterTemplate)
													.then(
															function(content) {
																$scope.checkboxElements = $scope.filterValue;
																$scope.filterContentHTML = content;
															});
											}
											
										} else if ($scope.filterType === "DATE") {
											if($scope.filterContentHTML===undefined){
												fetchTemplate(dateFilterTemplate)
													.then(
															function(content) {
																$scope.filterContentHTML = content;
															});
											}
										} else if ($scope.filterType === "BOOLEAN") {
											if($scope.filterContentHTML===undefined){
												fetchTemplate(booleanFilterTemplate)
													.then(
															function(content) {
																$scope.filterContentHTML = content;
															});
											}
										}
										// We grab the button
										clickedEl = element.children()[0];

										// Just to make sure.. had a bug where
										// key events were recorded twice
										angular.element(document).unbind(
												'click',
												$scope.externalClickListener);
										angular.element(document).unbind(
												'keydown',
												$scope.keyboardListener);

										// close if ESC key is pressed.
										if (e.keyCode === 27) {
											angular.element(
													$scope.filterContentDiv)
													.removeClass('show');
											angular.element(clickedEl)
													.removeClass(
															'buttonClicked');
											angular
													.element(document)
													.unbind(
															'click',
															$scope.externalClickListener);

											// clear the focused element;
											$scope
													.removeFocusStyle($scope.tabIndex);

											// close callback
											$scope.onClose({
												data : element
											});
											return true;
										}

										// The idea below was taken from another
										// multi-select directive -
										// https://github.com/amitava82/angular-multiselect
										// His version is awesome if you need a
										// more simple multi-select approach.

										// close
										if (angular.element(
												$scope.filterContentDiv)
												.hasClass('show')) {
											$scope.closeFilterContent();
											// close callback
											$scope.onClose({
												data : element
											});
										}
										// open
										else {
											helperItems = [];
											helperItemsLength = 0;

											$scope.openFilterContent();

											// open callback
											$scope.onOpen({
												data : element
											});
										}
									};

									// handle clicks outside the button / multi
									// select layer
									$scope.externalClickListener = function(e) {
										targetsArr = element
												.find(e.target.tagName);
										for (var i = 0; i < targetsArr.length; i++) {
											if (e.target == targetsArr[i]) {
												return;
											}
										}

										$scope.closeFilterContent();

										// close callback
										$timeout(function() {
											$scope.onClose({
												data : element
											});
										}, 0);
									};

								}
							};
						} ])
		.directive('ngCriteriaBar',
				[
						'$sce',
						'$timeout',
						'$templateCache',
						'$q',
						'$http',
						'$compile',
						'$filter',
						function($sce, $timeout, $templateCache, $q, $http,
								$compile, $filter) {
							return {
								restrict : 'AE',
								replace : true,
								scope : {
									// models
									criteriaBarConfig : '=',

									// callbacks
									doFilter : '&'
								},
								template : 
									'<div class="ng-criteria-bar row" >'
									+ '<ul class="list-inline">'
										+ '<li ng-repeat="criterion in criterions">'
											+ '<div data-ng-criteria data-criteria-config="criterion" data-on-filter-triggered="filterTriggered(criterion.currentFilter)" data-on-remove-criteria="removeFilter(criterion.name)"></div>'
										+ '</li>'
										+ '<li class="divider-vertical"></li>'
										+ '<li>'
											+'<span id="manage-filter-btn">'
												+ '<button type="button" class="btn btn-default" ng-click="toggleMoreContent( $event )">Manage filters <span class="caret"></span></button>'
												+ '<div class="more-content hide col-md-2 col-xs-3">'
													+ '<ul class="list-group">'
														+ '<li class="list-group-item" ng-repeat="criterion in criterions">'
															+ '<div class="checkbox">'
																+ '<label>'
																+ '<input type="checkbox" data-ng-model="criterion.displayed" data-ng-change="showHideCriterion(criterion)"> {{criterion.name}}'
																+ '</label>'
															+ '</div>'
														+ '</li>'
													+ '</ul>'
												+ '</div>'
											+ '</span>'
										+ '</li>'
										+ '<li>'
											+ '<div class="checkbox">'
												+ '<label>'
													+ '<input type="checkbox" data-ng-model="autoSearchEnable"> auto filter'
												+ '</label>'
											   + '</div>'
										+ '</li>'
										+ '<li>'
											+ '<button type="button" class="btn btn-primary" data-ng-click="doFilter(filters)"><i class="fa fa-search"></i></button>'
										+ '</li>'
									+ '</ul>'
								+ '</div>',
								link : function($scope, element, attrs) {
									$scope.criterions=$scope.criteriaBarConfig.criterions;
									$scope.visibleCriterions=[];
									$scope.inVisibleCriterions=[];
									$scope.moreContentElement=element.find(".more-content");
									
									$scope.isCriterionVisible=function(criterion){
										return $scope.visibleCriterions.indexOf(criterion) !== -1;
									};
									
									$scope.closeManageFilter=function(){
										$scope.moreContentElement.removeClass('show');
										$scope.moreContentElement.addClass('hide');
										angular.element(document).unbind('click',
												$scope.externalManageFilterClickListener);
									}
									
									$scope.openManageFilter=function(){
										$scope.moreContentElement.removeClass('hide');
										$scope.moreContentElement.addClass('show');
										angular.element(document).bind('click',
												$scope.externalManageFilterClickListener);
									}
									
									$scope.toggleMoreContent=function( $event ){
										//close
										if ($scope.moreContentElement
												.hasClass('show')) {
											$scope.closeManageFilter();
										}
										// open
										else {
											$scope.openManageFilter();
										}
									};
									
									$scope.showHideCriterion=function(criterion){
										if(criterion.displayed===false){
											$scope.removeFilter(criterion.name);
										}
									};
									
									$scope.filters=$scope.criteriaBarConfig.filters;
									$scope.autoSearchEnable=($scope.criteriaBarConfig.autoFilter !== undefined) ? $scope.criteriaBarConfig.autoFilter:false;
									$scope.filterTriggered=function(filterResult){
										var foundExistingFilter=undefined;
										console.log("Criteria bar watcher triggered:"+filterResult.type+' '+filterResult.field+' '+filterResult.value);
										foundExistingFilter=findFilterByFieldName(filterResult.field);
										if(foundExistingFilter!== undefined){
											//update existing filter
											console.log("update existing filter"+foundExistingFilter.field);
											foundExistingFilter.value=filterResult.value;
										}
										else {
											//add new filter
											$scope.filters.push(filterResult);
											console.log("Add new filter: "+ filterResult.field);
										}
										if($scope.autoSearchEnable){
											$scope.doFilter($scope.filters);
										}
									};
									
									findFilterByFieldName=function(filterFieldName){
										for (var i in $scope.filters) {
											  if($scope.filters[i].field===filterFieldName){
												return 	$scope.filters[i];
												}
											}
									};
									$scope.removeFilter=function(filterFieldName){
										for (var i in $scope.filters) {
											  if($scope.filters[i].field===filterFieldName){
												  $scope.filters.splice(i, 1);
												  $scope.doFilter($scope.filters);
												  console.log("Remove criterion from bar "+filterFieldName);
												}
											}
									};
									//handle external click for moreelement
									//manage filter content
									$scope.externalManageFilterClickListener = function(e) {
										targetsArr = element.find("#manage-filter-btn")
												.find(e.target.tagName);
										for (var i = 0; i < targetsArr.length; i++) {
											if (e.target == targetsArr[i]) {
												return;
											}
										}
										$scope.closeManageFilter();
									};
									
									}
								}
							}
						 ])
