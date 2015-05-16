App.controller('AdminCompaniesController',function ($scope,$rootScope,CompanyREST,ngTableParams,alertService,notifService,$filter) {
	$scope.page={"title":"Companies management","description":"Home page"};
	var allCompany=[];
	$scope.hasDatas=false;
	$scope.startIndex=0;
	$scope.endIndex=0;
	$scope.company={};
	$scope.company.contacts=[];
	$scope.addContact=function(){
		$scope.company.contacts.push({"name":"","email":"","phoneNumbers":[]});
		
	};
	$scope.addTel=function(contact){
		contact.phoneNumbers.push("");
	};
	$scope.deleteTel=function(contact,index){
		contact.phoneNumbers.splice(index, 1)
	};
	
	$scope.reset=function(){
		$scope.company={};
		$scope.company.registeredDate= new Date(moment())
		$scope.company.contacts=[];
	};
	
	$scope.postCompany = function(hideFn) {
		CompanyREST.save($scope.company).$promise.then(function(result) {
			alertService.show('info','Confirmation', 'Data saved');
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
			alertService.show('info','Created','Data updated');
			$scope.reset();
			$scope.tableParams.reload();
			hideFn();
		});
	};
	
	$scope.reset();
});


App.controller('AdminCompanyViewController',function ($scope, $rootScope,$http,CompanyREST,ngTableParams,$state){
	
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
	
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
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
});


App.controller('AdminCompanyQuickViewController',function ($scope,$http){
//	$scope.doFilterList($scope.criteriaBarFilter);
//	$scope.$on('criteriaDofilter', function(event, args) {
//		$scope.doFilterList(args);
//	});
	$scope.tableParams.settings().counts=[];
});


App.controller('AdminCompanyListController',function ($scope, $rootScope,$http,$state){
	 $scope.changeSelection = function(company) {
	      $state.go('admin.company.details',{ id:company.id });
	 };
	 $scope.tableParams.settings().counts=[10, 25, 50, 100];
});

App.controller('AdminCompanyOverviewController',function ($scope,company,CompanyREST){
	$scope.companyId=company.id;
	$scope.company=company;
	
	$scope.updateCompany = function() {
		var companyToUpdate={id:$scope.company.id,name:$scope.company.name,registeredDate:$scope.company.registeredDate,licenseEndDate:$scope.company.licenseEndDate,contacts:$scope.company.contacts};
		angular.forEach(companyToUpdate.contacts,function(contact){
			delete contact.searchField;
		});
		return CompanyREST.update({companyId :$scope.companyId},companyToUpdate).$promise.then(
		        //success
		        function( value ){
		        	 $scope.tableParams.reload();
		        },
		        //error
		        function( error ){/*Do something with error*/}
		      );
	};
	$scope.contactsManagerConfig={
			dataObject:$scope.company,
			update:$scope.updateCompany
	};
});