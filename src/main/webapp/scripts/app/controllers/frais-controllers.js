function FraisController($scope, $rootScope, NoteREST, alertService,
		ngTableParams, notifService, $upload,$modal,$http,$filter) {
	$rootScope.page = {
		"title" : "Frais",
		"description" : "Gerez vos notes de frais"
	};
	$scope.currentTab='myExpenses';
	$scope.approvementTotal=0;
	$scope.approvements=[];
	
	$scope.activateTab=function(tab){
		$scope.currentTab=tab;
	};
	$scope.isActiveTab=function(tab){
		return tab==$scope.currentTab;
	};
	$scope.initialActionLabel = "Ajouter une note";
	$scope.dateFormat = "dd MMMM yyyy";
	$scope.hasDatas = false;
	$scope.selectedNotes = [];
	$scope.ids = [];
	$scope.selectedFile=undefined;
	var allNote = [];
	
	$scope.refreshApproval=function(){
		$http.get(_contextPath+"app/api/frais/approval",{params:{"requesterId":_userId} })
		.success(function(data, status) {
			$scope.approvementTotal=data.totalCount;
			$scope.approvements=data.result;
		});
	};
	
	$scope.refreshApproval();
	$scope.approve=function(id){
		$http.put(_contextPath+"app/api/frais/approval/approve/"+parseInt(_userId)+"/"+id)
		.success(function(data, status) {
			alertService.show('success','Updated', 'Data has been updated');
			$scope.refreshApproval();
		});
	};
	$scope.reject=function(id){
		$http.put(_contextPath+"app/api/frais/approval/reject/"+parseInt(_userId)+"/"+id)
		.success(function(data, status) {
			alertService.show('success','Updated', 'Data has been updated');
			$scope.refreshApproval();
		});
	};
	$scope.tableFilter="";
	$scope.employeCriteriaConfig={
			name:"employe",
			defaultButtonLabel:'<i class="fa fa-user"></i>',
			filterType:"ARRAY",
			closeable:false,
			filterValue:[],
			buttonSelectedItemsFormater:function(data){
				if(data.name==""+_userId+""){
					return ' Me';
				}else {
					return ' '+getUserInitials(data.label);
				}
			},
			defaultSelectedItems:function(data){
				var items=[];
				angular.forEach(data,function(item){
					if(item.name==""+_userId+""){
						items.push(item);
					}
				});
				return items;
			},
			getData:function($defer){
				$http.get(_contextPath+"app/api/users/managed/"+_userId,{params:{"me":true} })
					.success(function(data, status) {
						$defer.resolve(data);
					});
			},
			currentFilter:{},
			displayed: true
	};
	$scope.descriptionCriteriaConfig={
			name:"description",
			defaultButtonLabel:"Description",
			filterType:"TEXT",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: false
	};
	
	$scope.dateCriteriaConfig={
			name:"date",
			defaultButtonLabel:"Date",
			filterType:"DATE",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: false
	};
	$scope.amountCriteriaConfig={
			name:"amount",
			defaultButtonLabel:"Amount",
			filterType:"COMPARATOR",
			closeable:true,
			filterValue:"",
			onFilter: function(value) {
				console.log('Filter text ['+value.field+'] searching: '+value.value);
			},
			currentFilter:{},
			displayed: false
	};
	$scope.criteriaBarConfig={
		criterions:[$scope.employeCriteriaConfig,$scope.descriptionCriteriaConfig,$scope.dateCriteriaConfig,$scope.amountCriteriaConfig],
		autoFilter:true,
		filters:[]
	};
	
	$scope.canPostNote=function(){
		return $scope.currentNote.date!='' && $scope.currentNote.date!=undefined && $scope.currentNote.amount!=0 && $scope.currentNote.amount!=undefined;
	};
	
	$scope.doFilter=function(data){
		var serverFilter={filter:data};
		$scope.tableFilter=JSON.stringify(serverFilter);
		$scope.refreshDatas();
	};

	
	var today = new Date();
	$scope.selectNote = function(note) {
		var index = $scope.selectedNotes.indexOf(note);
		if (index == -1) {
			$scope.selectedNotes.push(note);
		}
	};

	$scope.deSelectNote = function(note) {
		var index = $scope.selectedNotes.indexOf(note);
		if (index != -1) {
			$scope.selectedNotes.splice(index, 1);
		}
	};

	$scope.changeActionSelection = function() {
		$scope.reset();
		$scope.initialSelectionChanged = true;
	};

	$scope.reset = function() {
		$scope.initialSelectionChanged = false;
		$scope.selectedActionLabel = $scope.initialActionLabel;
		$scope.currentNote={};
		$scope.currentNote.id=undefined;
		$scope.currentNote.date = $filter("date")(Date.now(), 'yyyy-MM-dd');
		$scope.currentNote.description = undefined;
		$scope.currentNote.amount = 0;
		$scope.currentNote.attachements=undefined;
		$scope.edition = false;
		$scope.selectedFile=undefined;
	};

	function clone(obj) {
		if (null === obj || "object" != typeof obj)
			return obj;
		var copy = obj.constructor();
		for ( var attr in obj) {
			if (obj.hasOwnProperty(attr))
				copy[attr] = obj[attr];
		}
		return copy;
	}

	$scope.onFileSelect=function(file){
		$scope.selectedFile=file;
	};
	
	$scope.postNote=function(note,hideFn){
		 NoteREST.save(note).$promise.then(function(result) {
			alertService.show('success', 'Created', 'Nouvelle note sauvegarde');
			hideFn();
			$scope.reset();
			$scope.tableParams.reload();
		}, function(error) {
			console.log(error);
			notifService.notify('error', '' + error.status, error.data);
			hideFn();
			$scope.reset();
		});
	};
	
	$scope.postAttachement = function(hideFn) {
		if($scope.selectedFile){
			$scope.upload = $upload.upload({
				url : _contextPath+'app/api/attachements', // upload.php script, node.js route, or
											// servlet url
				file : $scope.selectedFile, // or list of files: $files for html5
											// only
			}).progress(
					function(evt) {
						console.log('percent: '	+ parseInt(100.0 * evt.loaded / evt.total));
					}).success(function(data, status, headers, config) {
				// file is uploaded successfully
				console.log(data);
				if(status==201){
					$scope.currentNote.attachementsIds=[];
					$scope.currentNote.attachementsIds.push(data);
					$scope.postNote($scope.currentNote,hideFn);
				}else {
					hideFn();
				}
			});
		}else {
			$scope.postNote($scope.currentNote,hideFn);
		}
	};
	$scope.putNote = function() {
		NoteREST.update(clone($scope.currentNote)).$promise.then(function(result) {
			notifService.notify('info', 'Created', 'Data updated');
			$scope.reset();
			$scope.tableParams.reload();
		});
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	};
	
	$scope.editNote = function(id) {
		$scope.edition = true;
		NoteREST.get({
			id : id
		}, function(data) {
			$scope.currentNote.id = data.id;
			$scope.currentNote.date = data.date;
			$scope.currentNote.amount = data.amount;
			$scope.currentNote.description = data.description;
			$scope.currentNote.attachements = data.attachements;
		}, function(error) {
			console.log(error);
			$scope.reset();
		});
	};

	$scope.deleteNote = function(id) {
		NoteREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			notifService.notify('info', 'Confirmation', 'Expense deleted');
		}, function(error) {
			console.log(error);
			notifService.notify('error', '' + error.status, error.data);
		});

	};
	$scope.reset();
	$scope.startIndex=0;
	$scope.endIndex=0;
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			date : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {
			if($scope.tableFilter!=="" && $scope.tableFilter!==undefined){
				NoteREST.get({
					page:params.$params.page-1,
					size:params.$params.count,
					sort:params.$params.sorting,
					filter:$scope.tableFilter
				}, function(data) {
					params.total(data.totalCount);
					$scope.startIndex=data.startIndex;
					$scope.endIndex=data.endIndex;
					if (data.totalCount >= 1) {
						$scope.hasDatas = true;
					} else {
						$scope.hasDatas = false;
					}
					allNote = data.result;
					// set new data
					$defer.resolve(data.result);
				});
			}
		}
	});

	$scope.checkboxes = {
		'checked' : false,
		items : {}
	};

	$scope.getNoteForId = function(array, id) {
		var findOne = {};
		angular.forEach(array, function(item) {
			if (id == item.id) {
				findOne = item;
			}
		});
		return findOne;
	};
	// watch for check all checkbox
	$scope.$watch('checkboxes.checked', function(value) {
		angular.forEach(allNote, function(item) {
			if (angular.isDefined(item.id)) {
				$scope.checkboxes.items[item.id] = value;
			}
		});
	});

	// watch for data checkboxes
	$scope.$watch('checkboxes.items', function(values) {
		if (!allNote) {
			return;
		}
		var checked = 0, unchecked = 0, total = allNote.length;
		angular.forEach(allNote, function(item) {
			checked += ($scope.checkboxes.items[item.id]) || 0;
			unchecked += (!$scope.checkboxes.items[item.id]) || 0;
			if ($scope.checkboxes.items[item.id]) {
				$scope.selectNote($scope.getNoteForId(allNote, item.id));
			} else {
				$scope.deSelectNote($scope.getNoteForId(allNote, item.id));
			}
		});
		if ((unchecked === 0) || (checked === 0)) {
			$scope.checkboxes.checked = (checked === total);
		}
		if (total === 0) {
			$scope.checkboxes.checked = false;
		}
		// grayed checkbox
		angular.element(document.getElementById("select_all")).prop("indeterminate", (checked !==0 && unchecked !== 0));
	}, true);

}