App.controller('CompanyInvoicesController',function ($scope,USERINFO){
	
});


App.controller('CompanyInvoicesQuickViewController',function ($scope,USERINFO,InvoicesREST,ngTableParams,$http,alertService){
	$scope.invoiceFilter="";
	$scope.invoiceFilterText="";
	$scope.newInvoice={};
	
	var fetchClients = function(queryParams) {
		return $http.get(
				_contextPath + "app/api/" + USERINFO.company.id + "/client", {
					params : {}
				}).then(function(response) {
					$scope.clients=response.data;
				});
	}; 
	fetchClients();
	$scope.createInvoice=function(hideFn){
		InvoicesREST.save($scope.newInvoice).$promise.then(function(result) {
	   		 hideFn();
	   		 alertService.show('info','Confirmation', 'Invoice created');
	   		$scope.tableParams.reload();
			});
	};
	$scope.filterInvoices=function(invoiceFilterText){
		$scope.invoiceFilter="{\"filter\":[{\"type\":\"TEXT\",\"field\":\"global\",\"value\":\""+invoiceFilterText+"\"}]}";
		$scope.tableParams.reload();
	};
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			id : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			InvoicesREST.getAll(
						{
							page:params.$params.page-1,
							size:params.$params.count,
							sort:params.$params.sorting,
							filter:$scope.invoiceFilter
						},function(data) {
					params.total(data.totalCount);
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if(data.totalCount>=1){
						$scope.hasDatas=true;
					}else {
						$scope.hasDatas=false;
					}
					// set new data
					$defer.resolve(data);
				});
		}});
});

App.controller('CompanyInvoicesOverviewController',function ($scope,USERINFO,invoice,alertService,InvoicesREST){
	$scope.invoice=invoice;
	var dateFormat="YYYY-MM-DD";
	$scope.addEmptyInvoiceItemRow=function(){
		$scope.invoice.invoiceItems.push({
			itemLabel:'',
			itemDescription:'',
			quantity:0,
			unitPrice:0
		});
	};
	$scope.deleteInvoiceItemRow=function(index){
		$scope.invoice.invoiceItems.splice(index,1);
	};
	
	$scope.updateInvoice=function(){
		var invoiceToSave=$scope.invoice;
		invoiceToSave.invoiceDate=moment(invoiceToSave.invoiceDate).format(dateFormat)
		invoiceToSave.dueDate=moment(invoiceToSave.dueDate).format(dateFormat)
		InvoicesREST.update(invoiceToSave).$promise.then(function(result){
			 alertService.show('success','Confirmation', 'Invoice updated');
		});
	}
});


