function FraisController($scope, $rootScope, NoteCRUDREST, alertService,
		ngTableParams, notifService, $upload,$modal) {
	$rootScope.page = {
		"title" : "Frais",
		"description" : "G�rer vos note frais"
	};
	$scope.initialActionLabel = "Ajouter une note";
	$scope.dateFormat = "dd MMMM yyyy";
	$scope.hasDatas = false;
	$scope.selectedNotes = [];
	$scope.ids = [];
	var allNote = [];

	var note = $scope.currentNote = {};
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
		note.id = undefined;
		note.date = today;
		note.description = '';
		note.amount = 0;
		note.attachements
		$scope.edition = false;
		$scope.selectedFile=[];
	};

	function clone(obj) {
		if (null == obj || "object" != typeof obj)
			return obj;
		var copy = obj.constructor();
		for ( var attr in obj) {
			if (obj.hasOwnProperty(attr))
				copy[attr] = obj[attr];
		}
		return copy;
	}

	$scope.onFileSelect=function(files){
		$scope.selectedFile.push(files[0]);
	}
	
	$scope.postNote=function(note,hideFn){
		 NoteCRUDREST.save(note).$promise.then(function(result) {
			notifService
					.notify('info', 'Created', 'Nouvelle note enregistr�');
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
		if($scope.selectedFile.length>0){
			$scope.upload = $upload.upload({
				url : _contextPath+'/app/api/attachements', // upload.php script, node.js route, or
											// servlet url
				file : $scope.selectedFile, // or list of files: $files for html5
											// only
			}).progress(
					function(evt) {
						console.log('percent: '
								+ parseInt(100.0 * evt.loaded / evt.total));
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
		NoteCRUDREST.update(clone(note)).$promise.then(function(result) {
			notifService.notify('info', 'Created', 'Modification effectu�');
			$scope.reset();
			$scope.tableParams.reload();
		});
	};
	
	$scope.refreshDatas=function(){
		$scope.tableParams.reload();
	}
	
	$scope.editNote = function(id) {
		$scope.edition = true;
		NoteCRUDREST.get({
			id : id
		}, function(data) {
			note.id = data.id;
			note.date = data.date;
			note.amount = data.amount;
			note.description = data.description;
			note.attachements = data.attachements;
		}, function(error) {
			console.log(error);
			$scope.reset();
		});
	};

	$scope.deleteNote = function(id) {
		NoteCRUDREST.remove({
			id : id
		}).$promise.then(function(result) {
			$scope.tableParams.reload();
			notifService.notify('info', 'Confirmation', 'Note supprimé');
		}, function(error) {
			console.log(error);
			notifService.notify('error', '' + error.status, error.data);
		});

	};
	$scope.reset();
	$scope.tableParams = new ngTableParams({
		page : 1, // show first page
		count : 10, // count per page
		sorting : {
			date : 'desc' // initial sorting
		}
	}, {
		total : 0, // length of data
		getData : function($defer, params) {

			NoteCRUDREST.get({
				page:params.$params.page-1,
				size:params.$params.count,
				sort:params.$params.sorting,
				filter:params.$params.filter
			}, function(data) {
				params.total(data.totalCount);
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
			;
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
		if ((unchecked == 0) || (checked == 0)) {
			$scope.checkboxes.checked = (checked == total);
		}
		if (total == 0) {
			$scope.checkboxes.checked = false;
		}
		// grayed checkbox
		angular.element(document.getElementById("select_all")).prop(
				"indeterminate", (checked != 0 && unchecked != 0));
	}, true);

}
