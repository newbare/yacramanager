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
			};
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
								template : 
										  '<span class="ng-criterion inlineBlock" data-ng-class="{active: active, noteditable: !isEditable, disabled: !isEditable}" data-ng-show="criteriaConfig.displayed" >'+
										'<div class="btn-group dropdown">'+
										 '<button type="button" class="criterion-btn dropdown-toggle"  data-toggle="dropdown" ng-bind-html="buttonLabel" data-ng-disabled="!isEditable"></button>'+
										 '<a href="" class="criterion-remove" data-ng-click="dismissCriteria(criteriaConfig.name)" data-ng-show="closeable" data-ng-disabled="!isEditable">'+
										 '<i class="fa fa-times-circle"></i>'+
										 '</a>'+
										'<div class="filter-content dropdown-menu" ng-html-compile="filterContentHTML">'+
										'</div>'+ 
										'</div>'+
										 '</span>',
								link : function($scope, element, attrs) {
									// default config values
									var textFilterTemplate = _contextPath + 'templates/criteria/criteria-text.tpl.html';
									var checkBoxFilterTemplate = _contextPath + 'templates/criteria/criteria-checkbox.tpl.html';
									var dateFilterTemplate = _contextPath + 'templates/criteria/criteria-date.tpl.html';
									var booleanFilterTemplate = _contextPath + 'templates/criteria/criteria-boolean.tpl.html';
									var comparatorFilterTemplate = _contextPath	+ 'templates/criteria/criteria-comparator.tpl.html';
									$scope.filterType = $scope.criteriaConfig.filterType;
									$scope.filterContentHTML=undefined;
									$scope.isEditable=($scope.criteriaConfig.editable!==undefined)?$scope.criteriaConfig.editable:true;
									$scope.active=false;
									$scope.dateRange={startDate: null, endDate: null};
									$scope.dateRangeOpts={
											timePicker: ($scope.criteriaConfig.timePicker !== undefined) ? $scope.criteriaConfig.timePicker : false,
											ranges:
											{
												'Last 7 Days': [moment().subtract('days', 6), moment()],
										         'Last 30 Days': [moment().subtract('days', 29), moment()],
										         'This Month': [moment().startOf('month'), moment().endOf('month')],
										         'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
											}
									};
									// templates initialisations
									
									$scope.reset=function(){
										if ($scope.filterType === "TEXT") {
											$scope.filterValue=undefined;
										}else if ($scope.filterType === "ARRAY") {
											
										}else if ($scope.filterType === "DATE") {
											$scope.dateSelector="byDate";
											$scope.uniqueDate=undefined;
											$scope.dateRange={startDate: null, endDate: null};
											$scope.dateRange.startDate=undefined;
											$scope.dateRange.endDate=undefined;
										}else if ($scope.filterType === "BOOLEAN") {
											$scope.booleanValue=undefined;
										}else if ($scope.filterType === "COMPARATOR") {
											$scope.filterValue=undefined;
										}
									};
									$scope.closeFilterContent=function(){
										$('[data-toggle="dropdown"]').parent().removeClass('open');
									};
									$scope.initialiseTemplates=function(){
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
																if($scope.criteriaConfig.defaultSelectedItems !== undefined &&  angular.isFunction($scope.criteriaConfig.defaultSelectedItems)){
																	defaultSelectedItems=$scope.criteriaConfig.defaultSelectedItems($scope.checkboxElements);
																	angular.forEach(defaultSelectedItems,function(item){
																		item.ticked=true;
																	});
																	$scope.onFilterCheckBox();
																}
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
										} else if ($scope.filterType === "COMPARATOR") {
											if($scope.filterContentHTML===undefined){
												$scope.operator="equals";
												fetchTemplate(comparatorFilterTemplate)
													.then(
															function(content) {
																$scope.filterContentHTML = content;
															});
											}
										}
										$scope.reset();
									};
									//get filter value datas
									if($scope.criteriaConfig.getData !== undefined &&  angular.isFunction($scope.criteriaConfig.getData)){
										var defer = $q.defer();
										defer.promise.then(function (data) {
											$scope.filterValue=data;
											$scope.initialiseTemplates();
										});
										$scope.criteriaConfig.getData(defer);
										
									}else {
										/*
										 * Common config for all type
										 */
										$scope.filterValue = $scope.criteriaConfig.filterValue;
										$scope.initialiseTemplates();
									}
									
									if ($scope.criteriaConfig.closeable !== undefined) {
										$scope.closeable = $scope.criteriaConfig.closeable;
									} else {
										$scope.closeable = true;
									}
									$scope.dismissCriteria = function(fieldName) {
										$scope.criteriaConfig.displayed=false;
										$scope.onRemoveCriteria(fieldName);
										debug("Remove criteria: "+fieldName);
									};
									var spanCaret= ($scope.isEditable=== true) ?' <span class="caret"></span>':'';
									
									$scope.computeButtonLabel = function(value) {
										$scope.buttonLabel = $sce.trustAsHtml($scope.criteriaConfig.defaultButtonLabel + ' '+ value + spanCaret);
									};
									$scope.resetButtonLabel = function() {
										$scope.buttonLabel = $sce
												.trustAsHtml($scope.criteriaConfig.defaultButtonLabel+ spanCaret);
									};
									$scope.resetButtonLabel();
									$scope.onFilterText = function() {
										var filter = {
											type  : "TEXT",
											field : $scope.criteriaConfig.name,
											value : $scope.filterValue
										};
										if ($scope.filterValue !== "" && $scope.filterValue !== undefined) {
											$scope.computeButtonLabel('('+ $scope.filterValue + ')');
										} else {
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										if($scope.criteriaConfig.onFilter!==undefined){
											$scope.criteriaConfig.onFilter(filter);
										}
										$scope.criteriaConfig.currentFilter=filter;
										$scope.onFilterTriggered(filter);
									};

									$scope.onFilterDate = function() {
										var computedValue;
										if($scope.dateSelector=="byDate"){
											computedValue=$scope.uniqueDate;
										}else {
											computedValue= {start : $scope.dateRange.startDate,end : $scope.dateRange.endDate};
										}
										var filter = {
											type  : ($scope.dateSelector=="byDate") ?"DATE":"DATE_RANGE",
											field : $scope.criteriaConfig.name,
											value :computedValue
										};
										if (($scope.dateSelector=="byRangeDate") && ($scope.dateRange.startDate !== undefined && $scope.dateRange.endDate !== undefined)) {
											$scope.computeButtonLabel('('+ $filter('date')($scope.dateRange.startDate,'shortDate')+ '-'+$filter('date')($scope.dateRange.endDate,'shortDate') + ')');
											
										}else if (($scope.dateSelector=="byDate") && ($scope.uniqueDate!==undefined)) {
											$scope.computeButtonLabel('('+ $filter('date')($scope.uniqueDate,'shortDate')+ ')');
										}else {
											filter.value=undefined;
											$scope.resetButtonLabel();
										}
										if(filter.value!==undefined){
											if($scope.criteriaConfig.onFilter!==undefined){
												$scope.criteriaConfig.onFilter(filter);
											}
											$scope.criteriaConfig.currentFilter=filter;
											$scope.onFilterTriggered(filter);
										}
										$scope.closeFilterContent();
									};
									
									$scope.onFilterComparator = function() {
										
										var comparatorValue;
										if ($scope.operator==="between") {
											comparatorValue= {
													start : $scope.comparatorStartValue,
													end : $scope.comparatorEndValue
												};
										}else {
											comparatorValue=$scope.comparatorValue; 
										}
										var filter = {
											type  : "COMPARATOR_"+$scope.operator.toUpperCase(),
											field : $scope.criteriaConfig.name,
											value : comparatorValue
										};
										if ($scope.comparatorValue !== undefined || ($scope.comparatorStartValue !== undefined && $scope.comparatorEndValue !== undefined)) {
											if ($scope.operator==="between") {
												if($scope.comparatorStartValue === undefined || $scope.comparatorEndValue === undefined){
													comparatorValue=undefined;
												}else {
													comparatorValue= {
															start : $scope.comparatorStartValue,
															end : $scope.comparatorEndValue
														};
												}
											}else if($scope.operator==="equals") {
												$scope.computeButtonLabel('(='+comparatorValue+')');
											}else if ($scope.operator==="lessthan") {
												$scope.computeButtonLabel('(<'+comparatorValue+')');
											}else if ($scope.operator==="greaterthan") {
												$scope.computeButtonLabel('(>'+comparatorValue+')');
											}
											if($scope.criteriaConfig.onFilter!==undefined){
												$scope.criteriaConfig.onFilter(filter);
											}
											$scope.criteriaConfig.currentFilter=filter;
											$scope.onFilterTriggered(filter);
										} else {
											filter.value=undefined;
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										
									};
									$scope.onFilterBoolean = function() {
										var filter = {
											type  : "BOOLEAN",
											field : $scope.criteriaConfig.name,
											value : $scope.booleanValue
										};
										if ($scope.booleanValue !== undefined && $scope.booleanValue !== "undefined" ) {
											$scope.computeButtonLabel('('+ $scope.booleanValue + ')');
										} else {
											$scope.resetButtonLabel();
										}
										$scope.closeFilterContent();
										if($scope.criteriaConfig.onFilter!==undefined && angular.isFunction($scope.criteriaConfig.onFilter)){
											$scope.criteriaConfig.onFilter(filter);
										}
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
															btnLabel = btnLabel	+ ' , ';
														}
														if($scope.criteriaConfig.buttonSelectedItemsFormater !== undefined &&  angular.isFunction($scope.criteriaConfig.buttonSelectedItemsFormater)){
															btnLabel = btnLabel	+ $scope.criteriaConfig.buttonSelectedItemsFormater(entry);
														}else {
															btnLabel = btnLabel	+ entry.name;
														}
														i++;
													}
												});
										if (btnLabel !== "") {
											$scope.computeButtonLabel(btnLabel);
										} else {
											$scope.resetButtonLabel();
										}
										var filter = {
											type  : "ARRAY",
											field : $scope.criteriaConfig.name,
											value : selected
										};
										if($scope.criteriaConfig.onFilter!==undefined){
											$scope.criteriaConfig.onFilter(filter);
										}
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
									'<div class="ng-criteria-bar row" >'+
									 '<ul class="list-inline">'+
										 '<li ng-repeat="criterion in criterions">'+
											 '<div data-ng-criteria data-criteria-config="criterion" data-on-filter-triggered="filterTriggered(criterion.currentFilter)" data-on-remove-criteria="removeFilter(criterion.name)"></div>'+
										 '</li>'+
										 '<li class="divider-vertical"></li>'+
										 '<li class="dropdown pull-right">'+
//										+ '<span id="manage-filter-btn">'
										'<div class="btn-group">'+
											 '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-filter"></i> <span class="caret"></span></button>'+
											 '<div class="more-content dropdown-menu">'+
											 '<form role="form">'+
											 '<div class="checkbox">'+
												 '<label>'+
													 '<input type="checkbox" data-ng-model="autoSearchEnable"> auto filter'+
												 '</label>'+
											    '</div>'+
											 '<div class="input-group">'+
													 '<input type="search" data-ng-model="managedFilterSearch" class="form-control" placeholder="Filter text" name="srch-term" id="srch-term">'+
													 '<span class="input-group-addon"><i class="fa fa-search"></i></span>'+
												 '</div>'+
												 '<br>'+
												 '<ul class="list-group">'+
													 '<li class="list-group-item" ng-repeat="criterion in managedCriterions | filter : managedFilterSearch">'+
														 '<div class="checkbox">'+
															 '<label>'+
																 '<input type="checkbox" data-ng-model="criterion.displayed" data-ng-change="showHideCriterion(criterion)"> {{criterion.name}}'+
															 '</label>'+
														 '</div>'+
													 '</li>'+
												 '</ul>'+
											 '</div>'+
											'</form>'+
											'</div>'+
//										+ '</span>'
										 '</li>'+
										 '<li class="pull-right">'+
											 '<button type="button" class="btn btn-primary" data-ng-click="doFilter(filters)"><i class="fa fa-search"></i></button>'+
										 '</li>'+
									 '</ul>'+
									
								 '</div>',
								link : function($scope, element, attrs) {
									debugEnabled=false;
									debug=function(text){
										if(debugEnabled){
											console.log(text);
										}
									};
									$scope.criterions=$scope.criteriaBarConfig.criterions;
									$scope.managedCriterions=[];
									angular.forEach($scope.criteriaBarConfig.criterions,
											function(criterion) {
												if (criterion.closeable) {
													$scope.managedCriterions.push(criterion);
												}
											});
									$scope.showHideCriterion=function(criterion){
										if(criterion.displayed===false){
											$scope.removeFilter(criterion.name);
										}
									};
									
									$scope.filters=$scope.criteriaBarConfig.filters;
									$scope.autoSearchEnable=($scope.criteriaBarConfig.autoFilter !== undefined) ? $scope.criteriaBarConfig.autoFilter:false;
									$scope.filterTriggered=function(filterResult){
										var foundExistingFilter;
										debug("Criteria bar watcher triggered:"+filterResult.type+' '+filterResult.field+' '+filterResult.value);
										foundExistingFilter=findFilterByFieldName(filterResult.field);
										if(foundExistingFilter!== undefined){
											if(filterResult.value===undefined){
												$scope.removeFilter(filterResult.field);
											}else{
												//update existing filter
												debug("update existing filter"+foundExistingFilter.field);
												foundExistingFilter.value=filterResult.value;
												if(filterResult.type.indexOf('COMPARATOR_') === 0){
													foundExistingFilter.type=filterResult.type;
												}
												if(filterResult.type.indexOf('DATE') === 0){
													foundExistingFilter.type=filterResult.type;
												}
											}
										}
										else {
											//add new filter
											$scope.filters.push(filterResult);
											debug("Add new filter: "+ filterResult.field);
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
												  debug("Remove criterion from bar "+filterFieldName);
												}
											}
									};
									}
								};
							}
						 ]);
