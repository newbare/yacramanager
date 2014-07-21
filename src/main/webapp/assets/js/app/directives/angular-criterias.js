angular.module('ngHtmlCompile', []).
    directive('ngHtmlCompile', function($compile) {
	return {
	    restrict: 'A',
	    link: function(scope, element, attrs) {
		scope.$watch(attrs.ngHtmlCompile, function(newValue, oldValue) {
		    element.html(newValue);
		    $compile(element.contents())(scope);
		});
	    }
	}
    });
angular.module('ng-criterias', [ 'ng' ])
	.directive('ngCriteria',
		[ '$sce', '$timeout','$templateCache','$q','$http','$compile', function($sce, $timeout,$templateCache,$q,$http,$compile) {
			return {
				restrict : 'AE',
				replace : true,
				scope :{
					// models
		            criteriaConfig  : '=',
		            
		            // callbacks
		            onClose         : '&',            
		            onOpen          : '&'
				},
				 template:
					 '<span class="ng-criteria inlineBlock">' +
						 '<div class="btn-group">'+
						 	'<button type="button" class="btn btn-default criteria-btn" ng-click="toggleFilterContent( $event )" ng-bind-html="buttonLabel"></button>'+
						 	'<button type="button" class="btn btn-default" data-ng-click="dismissCriteria()" data-ng-show="closeable">'+
						 		'<span aria-hidden="true">&times;</span>'+
						 	'</button>'+
						 '</div>'+
//						 '<div class="filter-content hide col-md-2 col-xs-3" ng-bind-html="filterContentHTML">' +
						 '<div class="filter-content hide col-md-2 col-xs-3" ng-html-compile="filterContentHTML">' +
//						 '<form data-role="search" data-ng-show="filterTypeText()">'+
//							'<div class="input-group">'+
//							'	<input type="search" data-ng-model="filterValue" class="form-control" placeholder="Filter text"'+
//							'	name="srch-term" id="srch-term">'+
//							'	<div class="input-group-btn">'+
//							'		<button class="btn btn-default" type="button" data-ng-click="onFilterText()">'+
//							'		<i class="fa fa-search"></i>'+
//							'		</button>'+
//							'	</div>'+
//							'</div>'+
//						'</form>'+
						 '</div>'+
					 '</span>',
					 
//			            '<span class="ng-criteria inlineBlock">' +        
//			                '<button type="button" class="btn btn-default criteria-btn" ng-click="toggleCheckboxes( $event ); refreshSelectedItems(); refreshButton();" ng-bind-html="buttonLabel">' +
//			                '</button>' +                              
//			                '<div class="filter-content">' +                        
//			                    '<form>' + 
//			                        '<div class="helperContainer" ng-if="displayHelper( \'filter\' ) || displayHelper( \'all\' ) || displayHelper( \'none\' ) || displayHelper( \'reset\' )">' +
//			                            '<div class="line" ng-if="displayHelper( \'all\' ) || displayHelper( \'none\' ) || displayHelper( \'reset\' )">' +
//			                                '<button type="button" ng-click="select( \'all\',   $event );"    class="helperButton" ng-if="!isDisabled && displayHelper( \'all\' )">   &#10003;&nbsp; Select All</button> ' +
//			                                '<button type="button" ng-click="select( \'none\',  $event );"   class="helperButton" ng-if="!isDisabled && displayHelper( \'none\' )">  &times;&nbsp; Select None</button>' +
//			                                '<button type="button" ng-click="select( \'reset\', $event );"  class="helperButton" ng-if="!isDisabled && displayHelper( \'reset\' )" style="float:right">&#8630;&nbsp; Reset</button>' +
//			                            '</div>' +
//			                            '<div class="line" style="position:relative" ng-if="displayHelper( \'filter\' )">' +
//			                                '<input placeholder="Search..." type="text" ng-click="select( \'filter\', $event )" ng-model="inputLabel.labelFilter" ng-change="updateFilter();$scope.getFormElements();" class="inputFilter" />' +
//			                                '<button type="button" class="clearButton" ng-click="inputLabel.labelFilter=\'\';updateFilter();prepareGrouping();prepareIndex();select( \'clear\', $event )">&times;</button> ' +
//			                            '</div>' +
//			                        '</div>' +
//			                        '<div class="checkBoxContainer" style="{{setHeight();}}">' +
//			                            '<div ng-repeat="item in filteredModel | filter:removeGroupEndMarker" class="multiSelectItem"' +
//			                                'ng-class="{selected: item[ tickProperty ], horizontal: orientationH, vertical: orientationV, multiSelectGroup:item[ groupProperty ], disabled:itemIsDisabled( item )}"' +
//			                                'ng-click="syncItems( item, $event, $index );"' + 
//			                                'ng-mouseleave="removeFocusStyle( tabIndex );">' + 
//			                                '<div class="acol" ng-if="item[ spacingProperty ] > 0" ng-repeat="i in numberToArray( item[ spacingProperty ] ) track by $index">&nbsp;</div>' +              
//			                                '<div class="acol">' +
//			                                    '<label>' +
//			                                        '<input class="checkbox focusable" type="checkbox" ng-disabled="itemIsDisabled( item )" ng-checked="item[ tickProperty ]" ng-click="syncItems( item, $event, $index )" />' +
//			                                        '<span ng-class="{disabled:itemIsDisabled( item )}" ng-bind-html="writeLabel( item, \'itemLabel\' )"></span>' +
//			                                    '</label>' +                                
//			                                '</div>' +
//			                                '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + 
//			                                '<span class="tickMark" ng-if="item[ groupProperty ] !== true && item[ tickProperty ] === true">&#10004;</span>' +
//			                            '</div>' +
//			                        '</div>' +
//			                    '<form>' +
//			                '</div>' +
//			            '</span>',

			        link: function ( $scope, element, attrs ) { 
			        	//default config values
			        	var textFilterTemplate=_contextPath+'/assets/others/criteria/criteria-text.tpl.html';
			        	var checkBoxFilterTemplate=_contextPath+'/assets/others/criteria/criteria-checkbox.tpl.html';
			        	
			        	if($scope.criteriaConfig.closeable !==undefined){
			        		$scope.closeable=$scope.criteriaConfig.closeable;
			        	}else {
			        		$scope.closeable=true;
						}
			        	
			        	
			        	$scope.dismissCriteria=function(){
			        		$scope.$destroy()
			        		element.remove();
			        	};
			        	
			        	/*
			        	 * Common config for all type
			        	 */
			        	$scope.filterValue=$scope.criteriaConfig.filterValue;
			        	$scope.computeButtonLabel=function(value){
			        		$scope.buttonLabel   = $sce.trustAsHtml($scope.criteriaConfig.defaultButtonLabel+': '+ value + ' <span class="caret"></span>');
			        	}
			        	$scope.resetButtonLabel=function(){
			        		$scope.buttonLabel= $sce.trustAsHtml($scope.criteriaConfig.defaultButtonLabel + ' <span class="caret"></span>');
			        	};
			        	$scope.resetButtonLabel();
			        	
			        	$scope.filterType= $scope.criteriaConfig.filterType;
			        	$scope.filterContentHTML;
			        	
			        	$scope.onFilterText=function(){
			        		var filter={field:$scope.criteriaConfig.name,value:$scope.filterValue};
			        		if($scope.filterValue!==""){
			        			$scope.computeButtonLabel('('+$scope.filterValue+')')
			        		}else {
			        			$scope.resetButtonLabel();
							} 
			        		$scope.closeFilterContent();
			        		$scope.criteriaConfig.onFilter(filter);
			        	 };
			        	 
			        	 $scope.onFilterCheckBox=function(){
			        		 var selected=[];
			        		 var btnLabel="";
			        		 var i=0;
			        		 angular.forEach($scope.checkboxElements,function(entry){
			        			 if(entry.ticked){
			        				 selected.push(entry);
			        				 if(i>0){
			        					 btnLabel=btnLabel+' , ';
			        				 }
			        				 btnLabel=btnLabel+entry.name;
			        			 }
			        			 i++;
			        		 });
			        		 if(btnLabel!==""){
			        			 $scope.computeButtonLabel(btnLabel)
			        		 }else {
			        			 $scope.resetButtonLabel();
							 }
			        		 var filter={field:$scope.criteriaConfig.name,value:selected};
				        		$scope.criteriaConfig.onFilter(filter);
				        	 };
			        	 
			        	function fetchTemplate(template) {
			                return $q.when($templateCache.get(template) || $http.get(template)).then(function (res) {
			                  if (angular.isObject(res)) {
			                    $templateCache.put(template, res.data);
			                    return res.data;
			                  }
			                  return res;
			                });
			              }
			        	
			        	$scope.openFilterContent=function(){
			        		angular.element( $scope.filterContentDiv ).addClass( 'show' );
		                    angular.element( $scope.filterContentDiv ).removeClass( 'hide' );
		                    angular.element( clickedEl ).addClass( 'buttonClicked' );                                        
		                    angular.element( document ).bind( 'click', $scope.externalClickListener );
			        	};
			        	
						$scope.closeFilterContent=function(){
							angular.element( $scope.filterContentDiv ).removeClass( 'show' );
		                    angular.element( $scope.filterContentDiv ).addClass( 'hide' );
		                    angular.element( clickedEl ).removeClass( 'buttonClicked' );                    
		                    angular.element( document ).unbind( 'click', $scope.externalClickListener );		        		
			        	};
			        	// UI operations to show/hide checkboxes based on click event..
			            $scope.toggleFilterContent = function( e ) {    

			                // We grab the checkboxLayer
			                $scope.filterContentDiv = element.children()[1];
			                
			                //compute content depending on the filter type 
			                if($scope.filterType === "text"){
				        		fetchTemplate(textFilterTemplate)
				        			.then(function(content){
				        				$scope.filterContentHTML=content;
				        			})
				        	}else if ($scope.filterType === "checkbox") {
				        		fetchTemplate(checkBoxFilterTemplate)
			        			.then(function(content){
			        				$scope.checkboxElements=$scope.filterValue;
			        				$scope.filterContentHTML=content;
			        			});
							}
			                // We grab the button
			                clickedEl = element.children()[0];

			                // Just to make sure.. had a bug where key events were recorded twice
			                angular.element( document ).unbind( 'click', $scope.externalClickListener );
			                angular.element( document ).unbind( 'keydown', $scope.keyboardListener );                                    

			                // close if ESC key is pressed.
			                if ( e.keyCode === 27 ) {
			                    angular.element( $scope.filterContentDiv ).removeClass( 'show' );                    
			                    angular.element( clickedEl ).removeClass( 'buttonClicked' );                    
			                    angular.element( document ).unbind( 'click', $scope.externalClickListener );

			                    // clear the focused element;
			                    $scope.removeFocusStyle( $scope.tabIndex );

			                    // close callback
			                    $scope.onClose( { data: element } );
			                    return true;
			                }                                

			                // The idea below was taken from another multi-select directive - https://github.com/amitava82/angular-multiselect 
			                // His version is awesome if you need a more simple multi-select approach.

			                // close
			                if ( angular.element( $scope.filterContentDiv ).hasClass( 'show' )) {                                          
			                	$scope.closeFilterContent();
			                    // close callback
			                    $scope.onClose( { data: element } );
			                } 
			                // open
			                else                 
			                {           
			                    helperItems = [];
			                    helperItemsLength = 0;

			                    $scope.openFilterContent();

			                    // open callback
			                    $scope.onOpen( { data: element } );
			                }                            
			            };
			            
			         // handle clicks outside the button / multi select layer
			            $scope.externalClickListener = function( e ) {                   
			                targetsArr = element.find( e.target.tagName );
			                for (var i = 0; i < targetsArr.length; i++) {                                        
			                    if ( e.target == targetsArr[i] ) {
			                        return;
			                    }
			                }

			                angular.element( $scope.filterContentDiv.previousSibling ).removeClass( 'buttonClicked' );                    
			                angular.element( $scope.filterContentDiv ).removeClass( 'show' );
			                angular.element( $scope.filterContentDiv ).addClass( 'hide' );
			                angular.element( document ).unbind( 'click', $scope.externalClickListener ); 
			                
			                // close callback                
			                $timeout( function() {
			                    $scope.onClose( { data: element } );
			                }, 0 );
			            };
			            
			            
			        }
				};
			}
		]);