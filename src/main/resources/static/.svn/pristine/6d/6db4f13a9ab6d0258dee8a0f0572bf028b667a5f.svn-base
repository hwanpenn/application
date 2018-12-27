/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * String controller
 */

var KisBpmStringPropertyCtrl = [ '$scope', '$modal', function ($scope,$modal) {
	if($scope.property.key=='oryx-formkeydefinition'){
		var opts = {
		        template:  'editor-app/configuration/properties/formkeydefinition-popup.html?version=' + Date.now(),
		        scope: $scope
		    };
		    $modal(opts);
	}
	
	$scope.shapeId = $scope.selectedShape.id;
	$scope.valueFlushed = false;
    /** Handler called when input field is blurred */
    $scope.inputBlurred = function() {
    	$scope.valueFlushed = true;
    	if ($scope.property.value) {
    		$scope.property.value = $scope.property.value.replace(/(<([^>]+)>)/ig,"");
    	}
        $scope.updatePropertyInModel($scope.property);
    };

    $scope.enterPressed = function(keyEvent) {
    	if (keyEvent && keyEvent.which === 13) {
    		keyEvent.preventDefault();
	        $scope.inputBlurred(); // we want to do the same as if the user would blur the input field
    	}
    };
    
    $scope.$on('$destroy', function controllerDestroyed() {
    	if(!$scope.valueFlushed) {
    		if ($scope.property.value) {
        		$scope.property.value = $scope.property.value.replace(/(<([^>]+)>)/ig,"");
        	}
    		$scope.updatePropertyInModel($scope.property, $scope.shapeId);
    	}
    });

}];

/*
 * Boolean controller
 */

var KisBpmBooleanPropertyCtrl = ['$scope', function ($scope) {

    $scope.changeValue = function() {
        if ($scope.property.key === 'oryx-defaultflow' && $scope.property.value) {
            var selectedShape = $scope.selectedShape;
            if (selectedShape) {
                var incomingNodes = selectedShape.getIncomingShapes();
                if (incomingNodes && incomingNodes.length > 0) {
                    // get first node, since there can be only one for a sequence flow
                    var rootNode = incomingNodes[0];
                    var flows = rootNode.getOutgoingShapes();
                    if (flows && flows.length > 1) {
                        // in case there are more flows, check if another flow is already defined as default
                        for (var i = 0; i < flows.length; i++) {
                            if (flows[i].resourceId != selectedShape.resourceId) {
                                var defaultFlowProp = flows[i].properties['oryx-defaultflow'];
                                if (defaultFlowProp) {
                                    flows[i].setProperty('oryx-defaultflow', false, true);
                                }
                            }
                        }
                    }
                }
            }
        }
        $scope.updatePropertyInModel($scope.property);
    };

}];

var KisBpmCheckBoxPropertyCtrl = ['$scope','$http', function ($scope,$http) {
	var userId =document.URL.split('&userId=');
	//修改样式
	angular.element('#checkboxmain').parent().parent().attr("style","width:80%;");
	angular.element('#checkboxmain').parent().parent().prev().attr("style","width:8%;margin:2px;");
	angular.element('#checkboxmain').parent().parent().parent().attr("style","width:100%;");
	var url = KISBPM.URL.getURL('getChannel?userId='+userId[1]);
	$http({method: 'GET', url:url }).
	 	success(function (data) {
	 		$scope.channels=data.results;
	 	}).error(function (data) {
	 			console.log('查询渠道报错');
	 });
	$scope.selected = [];
	   $scope.changeValue = function($event,id) {
	    	 var checkbox = $event.target;
	    	 var action = (checkbox.checked?'add':'remove');
	         if(action == 'add' && $scope.selected.indexOf(id) == -1){
	             $scope.selected.push(id);
	        }
	         if(action == 'remove' && $scope.selected.indexOf(id)!=-1){
	            var idx = $scope.selected.indexOf(id);
	            $scope.selected.splice(idx,1);
	         }
	         $scope.property.value=$scope.selected.toString();
	         $scope.updatePropertyInModel($scope.property); 
	    };
	    if($scope.property.value!=null&&$scope.property.value!=''){
	    	$scope.selected=$scope.property.value.split(",");
	    }
	    $scope.isSelected = function(id){
	    	//脏数据处理id变成了true,我日
	    	if(id==true){
	    		return true;
	    	}
	    	return $scope.selected.indexOf(id)>=0;
	    }	
}];

/*
 * Text controller
 */

var KisBpmTextPropertyCtrl = [ '$scope', '$modal', function($scope, $modal) {

    var opts = {
        template:  'editor-app/configuration/properties/text-popup.html?version=' + Date.now(),
        scope: $scope
    };

    // Open the dialog
    $modal(opts);
}];

var KisBpmTextPropertyPopupCtrl = ['$scope', function($scope) {
    
    $scope.save = function() {
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };

    $scope.close = function() {
        $scope.property.mode = 'read';
        $scope.$hide();
    };
}];

/*
 * lenovo
 */
var KisBpmformkeydefinitionPopupCtrl=['$scope','$http',function($scope,$http){
	 var url = KISBPM.URL.getURL('forms')
	 $http({method: 'GET', url:url }).
     success(function (data) {
    	 $scope.forms=data;
     }).
     error(function (data) {
       console.log('查询表单报错');
     });
	 
	 $scope.save = function() {
	        $scope.updatePropertyInModel($scope.property);
	        $scope.close();
	    };

	 $scope.close = function() {
	        $scope.property.mode = 'read';
	        $scope.$hide();
	    };
           
	
}];