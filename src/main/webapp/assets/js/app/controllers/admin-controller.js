function AdminController($scope,$rootScope) {
	$rootScope.page={"title":"Admin","description":"Configure application"}
}

function AdminHomeController($scope,$rootScope) {
	$scope.page={"title":"Admin board","description":"Home page"}
}

function AdminCompaniesController($scope,$rootScope,CompanyREST,ngTableParams,alertService,notifService) {
	$scope.page={"title":"Companies management","description":"Home page"};
	var allCompany=[];
	$scope.hasDatas=false;
	$scope.startIndex=0;
	$scope.endIndex=0;
	$scope.company={};
	$scope.company.contacts=[];
	$scope.addContact=function(){
		$scope.company.contacts.push({"email":"","numeroTelephone":""});
	};
	$scope.reset=function(){
		$scope.company={};
		$scope.company.contacts=[];
	};
	
	$scope.postCompany = function(hideFn) {
		CompanyREST.save($scope.company).$promise.then(function(result) {
			alertService.show('info','Confirmation', 'Donn� sauvegard�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	$scope.deleteCompany = function(id) {
		CompanyREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			alertService.show('info','Confirmation', 'Company supprimé');
		}, function(error) {
			console.log(error);
		});
	};
	$scope.putCompany = function() {
		CompanyREST.update($scope.company).$promise.then(function(result) {
			alertService.show('info','Created','Mise � jour effectu�');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	
	
}


function AdminCompanyViewController($scope, $rootScope,$http,CompanyREST,ngTableParams,$state){
	
	$scope.$state=$state;
	$scope.tableFilter="";
	$scope.nameCriteriaConfig={
			name:"name",
			defaultButtonLabel:"Name",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.licenseEndDateCriteriaConfig={
			name:"licenseEndDate",
			defaultButtonLabel:"License end date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	$scope.registeredDateCriteriaConfig={
			name:"registeredDate",
			defaultButtonLabel:"Registered date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: true
	};
	
	$scope.criteriaBarConfig={
		criterions:[$scope.nameCriteriaConfig,$scope.registeredDateCriteriaConfig,$scope.licenseEndDateCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			registeredDate : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			
			CompanyREST.get(
					{
						page:params.$params.page-1,
						size:params.$params.count,
						sort:params.$params.sorting,
						filter:$scope.tableFilter
					},function(data) {
				params.total(data.totalCount);
				$scope.startIndex=data.startIndex;
				$scope.endIndex=data.endIndex;
				if(data.totalCount>=1){
					$scope.hasDatas=true;
				}else {
					$scope.hasDatas=false;
				}
				allCompany=data.result;
				// set new data
				$defer.resolve(data.result);
			});
		}});
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
}


function AdminCompanyQuickViewController($scope,$http){
//	$scope.doFilterList($scope.criteriaBarFilter);
//	$scope.$on('criteriaDofilter', function(event, args) {
//		$scope.doFilterList(args);
//	});
	$scope.tableParams.settings().counts=[];
}


function AdminCompanyListController($scope, $rootScope,$http,$state){
	 $scope.changeSelection = function(company) {
	      $state.go('admin.company.details',{ id:company.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
};

function AdminCompanyOverviewController($scope,company){
	$scope.companyId=company.id;
	$scope.company=company;
}

function AdminSettingsController($scope,$rootScope) {
	$scope.page={"title":"Settings","description":"Home page"}
};

function LogsController($scope, LogsService,ngTableParams) {
    $scope.loggers = [];
    $scope.totalLoggersCount = 0;
    $scope.filter='';
    
    $scope.changeLevel = function (name, level) {
        LogsService.changeLevel({name: name, level: level}, function () {
        	$scope.doFilter();
        });
    };
    $scope.doFilter=function(){
    	$scope.tableParams.page(1); 
    	$scope.tableParams.reload();
    }
    
    $scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 100, // count per page
		sorting : {
			date : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			if(true){
				LogsService.findAll(
						{
							page:params.$params.page-1,
							size:params.$params.count,
							key:$scope.filter
						},function(data) {
					params.total(data.totalCount);
					$scope.totalLoggersCount=data.totalCount;
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					 $scope.loggers=data.result;
					// set new data
					$defer.resolve(data.result);
				});
			}
		}});
};

function MetricsController($scope, MetricsService,HealthCheckService) {

	$scope.healthCheck={};
	$scope.currentStackTrace=[];
	$scope.setStackTrace=function(key,error){
		$scope.currentStackTrace={"key":key,"error":error};
	}
    $scope.refresh = function() {
    	
    	HealthCheckService.check().then(function(promise) {
            $scope.healthCheck = promise;
        },function(promise) {
            $scope.healthCheck = promise.data;
        });

    	 $scope.metrics = MetricsService.get();

         $scope.metrics.$get({}, function(items) {

	            $scope.servicesStats = {};
	            $scope.cachesStats = {};
	            angular.forEach(items.timers, function(value, key) {
	                if (key.indexOf("web.api") != -1 || key.indexOf("service") != -1) {
	                    $scope.servicesStats[key] = value;
	                }

	                if (key.indexOf("net.sf.ehcache.Cache") != -1) {
	                    // remove gets or puts
	                    var index = key.lastIndexOf(".");
	                    var newKey = key.substr(0, index);

	                    // Keep the name of the domain
	                    index = newKey.lastIndexOf(".");
	                    $scope.cachesStats[newKey] = {
	                        'name': newKey.substr(index + 1),
	                        'value': value
	                    };
	                }
	            });
	        });
    };

    $scope.refresh();

//    $scope.threadDump = function() {
//        ThreadDumpService.dump().then(function(data) {
//            $scope.threadDump = data;
//
//            $scope.threadDumpRunnable = 0;
//            $scope.threadDumpWaiting = 0;
//            $scope.threadDumpTimedWaiting = 0;
//            $scope.threadDumpBlocked = 0;
//
//            angular.forEach(data, function(value, key) {
//                if (value.threadState == 'RUNNABLE') {
//                    $scope.threadDumpRunnable += 1;
//                } else if (value.threadState == 'WAITING') {
//                    $scope.threadDumpWaiting += 1;
//                } else if (value.threadState == 'TIMED_WAITING') {
//                    $scope.threadDumpTimedWaiting += 1;
//                } else if (value.threadState == 'BLOCKED') {
//                    $scope.threadDumpBlocked += 1;
//                }
//            });
//
//            $scope.threadDumpAll = $scope.threadDumpRunnable + $scope.threadDumpWaiting +
//                $scope.threadDumpTimedWaiting + $scope.threadDumpBlocked;
//
//        });
//    };

    $scope.getLabelClass = function(threadState) {
        if (threadState == 'RUNNABLE') {
            return "label-success";
        } else if (threadState == 'WAITING') {
            return "label-info";
        } else if (threadState == 'TIMED_WAITING') {
            return "label-warning";
        } else if (threadState == 'BLOCKED') {
            return "label-danger";
        }
    };
};