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
 * Assignment
 */
var KisBpmAssignmentCtrl = [ '$scope', '$modal', function($scope, $modal) {

    // Config for the modal window
    var opts = {
        template:  'editor-app/configuration/properties/assignment-popup.html?version=' + Date.now(),
        scope: $scope
    };
    // Open the dialog
    $modal(opts);
}];

var KisBpmAssignmentPopupCtrl = [ '$scope','$modal','$http', function($scope,$modal,$http) {
    // Put json representing assignment on scope
    if ($scope.property.value !== undefined && $scope.property.value !== null
        && $scope.property.value.assignment !== undefined
        && $scope.property.value.assignment !== null) 
    {
        $scope.assignment = $scope.property.value.assignment;
    } else {
        $scope.assignment = {};
    }

    if ($scope.assignment.candidateUsers == undefined || $scope.assignment.candidateUsers.length == 0)
    {
    	$scope.assignment.candidateUsers = [];
    	$scope.candidateUsersValue='';
    }else{
    	var tmpcandidateUsers=[];
    	var tmpcandidateUsersName=[];
    	for(var i = 0; i < $scope.assignment.candidateUsers.length; i++){
    		if(!$scope.assignment.candidateUsers[i].name==''){
    		tmpcandidateUsers.push($scope.assignment.candidateUsers[i].value);
    		tmpcandidateUsersName.push($scope.assignment.candidateUsers[i].name);
    		}
		}
    	$scope.candidateUsersValue=tmpcandidateUsers.toString();
    	$scope.candidateUsersNameValue=tmpcandidateUsersName.toString();
    }
    
    if ($scope.assignment.candidateGroups == undefined || $scope.assignment.candidateGroups.length == 0)
    {
    	$scope.assignment.candidateGroups = [];
    	$scope.candidateGroupsValue='';
    }else{
    	var tmpcandidateGroups=[];
    	var tmpcandidateGroupsName=[];
    	for(var i = 0; i < $scope.assignment.candidateGroups.length; i++){
    		if(!$scope.assignment.candidateGroups[i].name==''){
    			tmpcandidateGroups.push($scope.assignment.candidateGroups[i].value);
        		tmpcandidateGroupsName.push($scope.assignment.candidateGroups[i].name);
    		}
		}
    	$scope.candidateGroupsValue=tmpcandidateGroups.toString();
    	$scope.candidateGroupsNameValue=tmpcandidateGroupsName.toString();
    }

    if ($scope.assignment.backupCandidateGroups == undefined || $scope.assignment.backupCandidateGroups.length == 0)
        {
        	$scope.assignment.backupCandidateGroups = [];
        	$scope.backupCandidateGroupsValue='';
        }else{
        	var tmpBackupCandidateGroups=[];
        	var tmpBackupCandidateGroupsName=[];
        	for(var i = 0; i < $scope.assignment.backupCandidateGroups.length; i++){
        		if(!$scope.assignment.backupCandidateGroups[i].name==''){
        			tmpBackupCandidateGroups.push($scope.assignment.backupCandidateGroups[i].value);
            		tmpBackupCandidateGroupsName.push($scope.assignment.backupCandidateGroups[i].name);
        		}
    		}
        	$scope.backupCandidateGroupsValue=tmpBackupCandidateGroups.toString();
        	$scope.backupCandidateGroupsNameValue=tmpBackupCandidateGroupsName.toString();
        }

    if ($scope.assignment.candidateRules == undefined || $scope.assignment.candidateRules.length == 0)
    {
    	$scope.assignment.candidateRules = [];
    	$scope.candidateRulesValue='';
    }else{
    	var tmpcandidateRules=[];
    	var tmpcandidateRulesName=[];
    	for(var i = 0; i < $scope.assignment.candidateRules.length; i++){
    		if(!$scope.assignment.candidateRules[i].name==''){
    		tmpcandidateRules.push($scope.assignment.candidateRules[i].value);
    		tmpcandidateRulesName.push($scope.assignment.candidateRules[i].name);
    		}
		}
    	$scope.candidateRulesValue=tmpcandidateRules.toString();
    	$scope.candidateRulesNameValue=tmpcandidateRulesName.toString();
    }
    
    $scope.save = function() {
        $scope.property.value = {};
        handleAssignmentInput($scope);
        $scope.property.value.assignment = $scope.assignment;
        $scope.updatePropertyInModel($scope.property);
        $scope.close();
    };
    // Close button handler
    $scope.close = function() {
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
    
    $scope.candidateUsers=function(data,dataName){
    	$scope.candidateUsersValue=data.toString();
    	$scope.candidateUsersNameValue=dataName.toString();
    }
    
    $scope.candidateGroups=function(data,dataName){
    	$scope.candidateGroupsValue=data.toString();
    	$scope.candidateGroupsNameValue=dataName.toString();
    }

    $scope.backupCandidateGroups=function(data,dataName){
    	$scope.backupCandidateGroupsValue=data.toString();
    	$scope.backupCandidateGroupsNameValue=dataName.toString();
    }
    
    $scope.candidateRules=function(data,dataName){
    	$scope.candidateRulesValue=data.toString();
    	$scope.candidateRulesNameValue=dataName.toString();
    }
    
    var handleAssignmentInput = function($scope) {
    	
    	if($scope.candidateGroupsNameValue!=''&&$scope.candidateGroupsNameValue!=undefined){
    		$scope.assignment.candidateGroups=[];
    		var tempGroups=$scope.candidateGroupsValue.split(',');
    		var tempGroupsName=$scope.candidateGroupsNameValue.split(',');
    		for(var i = 0; i < tempGroups.length; i++){
    			var Gruop={value:tempGroups[i],name:tempGroupsName[i]};
    			$scope.assignment.candidateGroups.push(Gruop);
    		}
    	}else{
    		$scope.assignment.candidateGroups = undefined;
    	}

    	if($scope.backupCandidateGroupsNameValue!=''&&$scope.backupCandidateGroupsNameValue!=undefined){
    		$scope.assignment.backupCandidateGroups=[];
    		var tempBackupGroups=$scope.backupCandidateGroupsValue.split(',');
    		var tempBackupGroupsName=$scope.backupCandidateGroupsNameValue.split(',');
    		for(var i = 0; i < tempBackupGroups.length; i++){
    			var Gruop={value:tempBackupGroups[i],name:tempBackupGroupsName[i]};
    			$scope.assignment.backupCandidateGroups.push(Gruop);
    		}
    	}else{
    		$scope.assignment.backupCandidateGroups = undefined;
    	}
    	
    	if($scope.candidateUsersNameValue!=''&&$scope.candidateUsersNameValue!=undefined){
    		$scope.assignment.candidateUsers=[];
    		var tempUsers=$scope.candidateUsersValue.split(',');
    		var tempUsersName=$scope.candidateUsersNameValue.split(',');
    		for(var i = 0; i < tempUsers.length; i++){
    			var user={value:tempUsers[i],name:tempUsersName[i]};
    			$scope.assignment.candidateUsers.push(user);
    		}
    	}else{
    		$scope.assignment.candidateUsers = undefined;
    	} 
    	
    	if($scope.candidateRulesNameValue!=''&&$scope.candidateRulesNameValue!=undefined){
    		$scope.assignment.candidateRules=[];
    		var tempRules=$scope.candidateRulesValue.split(',');
    		var tempRulesName=$scope.candidateRulesNameValue.split(',');
    		for(var i = 0; i < tempRules.length; i++){
    			var rules={value:tempRules[i],name:tempRulesName[i]};
    			$scope.assignment.candidateRules.push(rules);
    		}
    	}else{
    		$scope.assignment.candidateRules = undefined;
    	} 
    	
    };
    //查询侯选组
    $scope.candidateGroupCtrl =function(){
    	var opts = {
    	        template:  'editor-app/configuration/properties/candidateGroup-popup.html?version=' + Date.now(),
    	        scope: $scope
    	    };
    	    // Open the dialog
    	$modal(opts);
    }
    //查询备选侯选组
    $scope.backupCandidateGroupCtrl =function(){
    	var opts = {
    	        template:  'editor-app/configuration/properties/backupCandidateGroup-popup.html?version=' + Date.now(),
    	        scope: $scope
    	    };
    	    // Open the dialog
    	$modal(opts);
    }
    //查询候选规则
    $scope.candidateRuleCtrl =function(){
    	var opts = {
    	        template:  'editor-app/configuration/properties/candidateRule-popup.html?version=' + Date.now(),
    	        scope: $scope
    	    };
    	$modal(opts);
    }
    //查询候选人
    $scope.candidateUserCtrl =function(){
    	var opts = {
    	        template:  'editor-app/configuration/properties/candidateUser-popup.html?version=' + Date.now(),
    	        scope: $scope
    	    };
    	$modal(opts);
    }
    $scope.deleteAllUser = function() {
    	$scope.candidateUsersValue='';
    	$scope.candidateUsersNameValue='';
    }
    $scope.deleteAllGroup = function() {
    	$scope.candidateGroupsValue='';
    	$scope.candidateGroupsNameValue='';
    }
    $scope.deleteBackupGroup = function() {
      	$scope.backupCandidateGroupsValue='';
       	$scope.backupCandidateGroupsNameValue='';
    }
    $scope.deleteAllRule = function() {
        $scope.candidateRulesValue='';
        $scope.candidateRulesNameValue='';
    }
    
}];

//侯选组表单
var KisBpmcandidateGroupPopupCtrl = ['$scope', '$modal', '$http', function($scope, $modal,$http) {
	var userId =document.URL.split('&userId=');	
	$scope.pageSize = 10;
	$scope.setName = function(name) {
		$scope.getSort=name;
	}
	$scope.getSort='job';
	$scope.search = function() {
		var searchparam=$scope.searchparam;
		if($scope.getSort=='job'){
			var joburl = KISBPM.URL.getURL('getJob?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
			 $http({method: 'GET', url:joburl }).
			 	success(function (data) {
			 			$scope.jobs=data;
			 			$scope.jobpages = Math.ceil($scope.jobs.totalNumber / $scope.pageSize); //分页数
			 			$scope.jobnewPages = $scope.jobpages > 5 ? 5 : $scope.jobpages;
			 			$scope.jobpageList = [];
			 			$scope.jobselPage = 1;
			 			$scope.selSort='job';
			 			for (var i = 0; i < $scope.jobnewPages; i++) {
			 				$scope.jobpageList.push(i + 1);
			 			};
			 	}).
			 		error(function (data) {
			 			console.log('查询岗位报错');
			 });
		}else if($scope.getSort=='dept'){
			var depturl = KISBPM.URL.getURL('getDept?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
			 $http({method: 'GET', url:depturl }).
			 	success(function (data) {
			 			$scope.depts=data;
			 			$scope.deptpages = Math.ceil($scope.depts.totalNumber / $scope.pageSize); //分页数
			 			$scope.deptnewPages = $scope.deptpages > 5 ? 5 : $scope.deptpages;
			 			$scope.deptpageList = [];
			 			$scope.deptselPage = 1;
			 			$scope.selSort='dept';
			 			for (var i = 0; i < $scope.deptnewPages; i++) {
			 				$scope.deptpageList.push(i + 1);
			 			};
			 	}).
			 		error(function (data) {
			 			console.log('查询部门报错');
			 });
			 }else if($scope.getSort=='role'){
			  var roleurl = KISBPM.URL.getURL('getRole?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
				 $http({method: 'GET', url:roleurl }).
				 	success(function (data) {
				 			$scope.roles=data;
				 			$scope.rolepages = Math.ceil($scope.roles.totalNumber / $scope.pageSize); //分页数
				 			$scope.rolenewPages = $scope.rolepages > 5 ? 5 : $scope.rolepages;
				 			$scope.rolepageList = [];
				 			$scope.roleselPage = 1;
				 			$scope.selSort='role';
				 			for (var i = 0; i < $scope.rolenewPages; i++) {
				 				$scope.rolepageList.push(i + 1);
				 			};
				 	}).
				 		error(function (data) {
				 			console.log('查询角色报错');
				 });
			 }else{
				 alert('系统出错');
			 }
	}
	 
	$scope.setData = function (page ,sort,keyword) {
		if(sort=='job'){
			var firstNumber=(page-1)*10;
			var newjoburl=KISBPM.URL.getURL('getJob?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newjoburl }).
		 	success(function (data) {
		 			$scope.jobs.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询岗位报错');
		 		});
		}else if(sort=='dept'){
			var firstNumber=(page-1)*10;
			var newdepturl=KISBPM.URL.getURL('getDept?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newdepturl }).
		 	success(function (data) {
		 			$scope.depts.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询部门报错');
		 		});
			
		}else if(sort=='role'){
			var firstNumber=(page-1)*10;
			var newroleurl=KISBPM.URL.getURL('getRole?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newroleurl }).
		 	success(function (data) {
		 			$scope.roles.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询角色报错');
		 		});
		}
		};
		 
	$scope.selectPage = function (page,sort) {
		//不能小于1大于最大
		if(sort=='job'){
			if (page < 1 || page > $scope.jobpages) return;
		}else if(sort=='role'){
			if (page < 1 || page > $scope.rolepages) return;
		}else if(sort=='dept'){
			if (page < 1 || page > $scope.deptpages) return;
		}
		
		//最多显示分页数5
		if (page > 2) {//因为只显示5个页数，大于2页开始分页转换
			if(sort=='job'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.jobpages ? $scope.jobpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.jobpageList = newpageList;
			}else if(sort=='role'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.rolepages ? $scope.rolepages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.rolepageList = newpageList;
			}else if(sort=='dept'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.deptpages ? $scope.deptpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.deptpageList = newpageList;
			}
		}
		if(sort=='job'){
			$scope.jobselPage = page;
		}else if(sort=='role'){
			$scope.roleselPage = page;
		}else if(sort=='dept'){
			$scope.deptselPage = page;
		}
		$scope.selSort = sort;
		var searchparam=$scope.searchparam;
		$scope.setData(page,sort,searchparam);
		$scope.isActivePage(page,sort);
	};
	$scope.isActivePage = function (page,sort) {
		if(sort=='job'){
			return $scope.jobselPage == page ;
		}else if(sort=='role'){
			return $scope.roleselPage == page ;
		}
		else if(sort=='dept'){
			return $scope.deptselPage == page ;
		}
	};
	if($scope.candidateGroupsNameValue!=''&&$scope.candidateGroupsNameValue!=undefined){
		$scope.selected = $scope.candidateGroupsValue.split(",") ; 
		$scope.selectedName = $scope.candidateGroupsNameValue.split(",") ;
	}else{
		$scope.selected = [] ; 
		$scope.selectedName = [] ;
	}  
    $scope.isChecked = function(id){  
        return $scope.selected.indexOf(id) >= 0 ;  
    } ;  
      
    $scope.updateSelection = function($event,id,name){  
        var checkbox = $event.target ;  
        var checked = checkbox.checked ;  
        if(checked){  
            $scope.selected.push(id) ;
            $scope.selectedName.push(name);
        }else{  
            var idx = $scope.selected.indexOf(id) ;
            var idy = $scope.selectedName.indexOf(name) ;
            $scope.selected.splice(idx,1) ;  
            $scope.selectedName.splice(idy,1) ;  
        }  
    } ; 
	
	$scope.save = function() {
		$scope.candidateGroups($scope.selected,$scope.selectedName);
		$scope.$hide();
	}
		
	$scope.close = function() {
		$scope.$hide();
	}
	
	$scope.Previous = function (sort) {
		if(sort=='job'){
			$scope.selectPage($scope.jobselPage - 1,sort);
		}else if(sort=='role'){
			$scope.selectPage($scope.roleselPage - 1,sort);
		}
		else if(sort=='dept'){
			$scope.selectPage($scope.deptselPage - 1,sort);
		}
	}
		//下一页
	$scope.Next = function (sort) {
		if(sort=='job'){
			$scope.selectPage($scope.jobselPage + 1,sort);
		}else if(sort=='role'){
			$scope.selectPage($scope.roleselPage + 1,sort);
		}
		else if(sort=='dept'){
			$scope.selectPage($scope.deptselPage + 1,sort);
		}
	};
	
}];


//备选组表单
var KisBpmBackupCandidateGroupPopupCtrl = ['$scope', '$modal', '$http', function($scope, $modal,$http) {
	var userId =document.URL.split('&userId=');
	$scope.pageSize = 10;
	$scope.setName = function(name) {
		$scope.getSort=name;
	}
	$scope.getSort='job';
	$scope.search = function() {
		var searchparam=$scope.searchparam;
		if($scope.getSort=='job'){
			var joburl = KISBPM.URL.getURL('getJob?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
			 $http({method: 'GET', url:joburl }).
			 	success(function (data) {
			 			$scope.jobs=data;
			 			$scope.jobpages = Math.ceil($scope.jobs.totalNumber / $scope.pageSize); //分页数
			 			$scope.jobnewPages = $scope.jobpages > 5 ? 5 : $scope.jobpages;
			 			$scope.jobpageList = [];
			 			$scope.jobselPage = 1;
			 			$scope.selSort='job';
			 			for (var i = 0; i < $scope.jobnewPages; i++) {
			 				$scope.jobpageList.push(i + 1);
			 			};
			 	}).
			 		error(function (data) {
			 			console.log('查询岗位报错');
			 });
		}else if($scope.getSort=='dept'){
			var depturl = KISBPM.URL.getURL('getDept?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
			 $http({method: 'GET', url:depturl }).
			 	success(function (data) {
			 			$scope.depts=data;
			 			$scope.deptpages = Math.ceil($scope.depts.totalNumber / $scope.pageSize); //分页数
			 			$scope.deptnewPages = $scope.deptpages > 5 ? 5 : $scope.deptpages;
			 			$scope.deptpageList = [];
			 			$scope.deptselPage = 1;
			 			$scope.selSort='dept';
			 			for (var i = 0; i < $scope.deptnewPages; i++) {
			 				$scope.deptpageList.push(i + 1);
			 			};
			 	}).
			 		error(function (data) {
			 			console.log('查询部门报错');
			 });
			 }else if($scope.getSort=='role'){
			  var roleurl = KISBPM.URL.getURL('getRole?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
				 $http({method: 'GET', url:roleurl }).
				 	success(function (data) {
				 			$scope.roles=data;
				 			$scope.rolepages = Math.ceil($scope.roles.totalNumber / $scope.pageSize); //分页数
				 			$scope.rolenewPages = $scope.rolepages > 5 ? 5 : $scope.rolepages;
				 			$scope.rolepageList = [];
				 			$scope.roleselPage = 1;
				 			$scope.selSort='role';
				 			for (var i = 0; i < $scope.rolenewPages; i++) {
				 				$scope.rolepageList.push(i + 1);
				 			};
				 	}).
				 		error(function (data) {
				 			console.log('查询角色报错');
				 });
			 }else{
				 alert('系统出错');
			 }
	}

	$scope.setData = function (page ,sort,keyword) {
		if(sort=='job'){
			var firstNumber=(page-1)*10;
			var newjoburl=KISBPM.URL.getURL('getJob?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newjoburl }).
		 	success(function (data) {
		 			$scope.jobs.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询岗位报错');
		 		});
		}else if(sort=='dept'){
			var firstNumber=(page-1)*10;
			var newdepturl=KISBPM.URL.getURL('getDept?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newdepturl }).
		 	success(function (data) {
		 			$scope.depts.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询部门报错');
		 		});

		}else if(sort=='role'){
			var firstNumber=(page-1)*10;
			var newroleurl=KISBPM.URL.getURL('getRole?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
			$http({method: 'GET', url:newroleurl }).
		 	success(function (data) {
		 			$scope.roles.items=data.items;
		 	}).
		 		error(function (data) {
		 			console.log('查询角色报错');
		 		});
		}
		};

	$scope.selectPage = function (page,sort) {
		//不能小于1大于最大
		if(sort=='job'){
			if (page < 1 || page > $scope.jobpages) return;
		}else if(sort=='role'){
			if (page < 1 || page > $scope.rolepages) return;
		}else if(sort=='dept'){
			if (page < 1 || page > $scope.deptpages) return;
		}

		//最多显示分页数5
		if (page > 2) {//因为只显示5个页数，大于2页开始分页转换
			if(sort=='job'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.jobpages ? $scope.jobpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.jobpageList = newpageList;
			}else if(sort=='role'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.rolepages ? $scope.rolepages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.rolepageList = newpageList;
			}else if(sort=='dept'){
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.deptpages ? $scope.deptpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.deptpageList = newpageList;
			}
		}
		if(sort=='job'){
			$scope.jobselPage = page;
		}else if(sort=='role'){
			$scope.roleselPage = page;
		}else if(sort=='dept'){
			$scope.deptselPage = page;
		}
		$scope.selSort = sort;
		var searchparam=$scope.searchparam;
		$scope.setData(page,sort,searchparam);
		$scope.isActivePage(page,sort);
	};
	$scope.isActivePage = function (page,sort) {
		if(sort=='job'){
			return $scope.jobselPage == page ;
		}else if(sort=='role'){
			return $scope.roleselPage == page ;
		}
		else if(sort=='dept'){
			return $scope.deptselPage == page ;
		}
	};
	if($scope.backupCandidateGroupsNameValue!=''&&$scope.backupCandidateGroupsNameValue!=undefined){
		$scope.selected = $scope.backupCandidateGroupsValue.split(",") ;
		$scope.selectedName = $scope.backupCandidateGroupsNameValue.split(",") ;
	}else{
		$scope.selected = [] ;
		$scope.selectedName = [] ;
	}
    $scope.isChecked = function(id){
        return $scope.selected.indexOf(id) >= 0 ;
    } ;

    $scope.updateSelection = function($event,id,name){
        var checkbox = $event.target ;
        var checked = checkbox.checked ;
        if(checked){
            $scope.selected.push(id) ;
            $scope.selectedName.push(name);
        }else{
            var idx = $scope.selected.indexOf(id) ;
            var idy = $scope.selectedName.indexOf(name) ;
            $scope.selected.splice(idx,1) ;
            $scope.selectedName.splice(idy,1) ;
        }
    } ;

	$scope.save = function() {
		$scope.backupCandidateGroups($scope.selected,$scope.selectedName);
		$scope.$hide();
	}

	$scope.close = function() {
		$scope.$hide();
	}

	$scope.Previous = function (sort) {
		if(sort=='job'){
			$scope.selectPage($scope.jobselPage - 1,sort);
		}else if(sort=='role'){
			$scope.selectPage($scope.roleselPage - 1,sort);
		}
		else if(sort=='dept'){
			$scope.selectPage($scope.deptselPage - 1,sort);
		}
	}
		//下一页
	$scope.Next = function (sort) {
		if(sort=='job'){
			$scope.selectPage($scope.jobselPage + 1,sort);
		}else if(sort=='role'){
			$scope.selectPage($scope.roleselPage + 1,sort);
		}
		else if(sort=='dept'){
			$scope.selectPage($scope.deptselPage + 1,sort);
		}
	};

}];

//对应规则表单
var KisBpmcandidateRulePopupCtrl = ['$scope', '$modal', '$http', function($scope, $modal,$http) {
	var userId =document.URL.split('&userId=');	
	var ruleurl = KISBPM.URL.getURL('getRule?firstNumber=0&pageSize=10&userId='+userId[1]);
	 $http({method: 'GET', url:ruleurl }).
	 	success(function (data) {
	 			$scope.rules=data;
	 	}).
	 		error(function (data) {
	 			console.log('查询规则报错');
	 });
	 
	 $scope.save = function() {
		 $scope.candidateRules($scope.selected,$scope.selectedName);
		 $scope.$hide();
		}
			
	 $scope.close = function() {
			$scope.$hide();
		}
	if($scope.candidateRulesNameValue!=''&&$scope.candidateRulesNameValue!=undefined){
		$scope.selected = $scope.candidateRulesValue.split(",") ; 
		$scope.selectedName = $scope.candidateRulesNameValue.split(",") ;
	}else{
		$scope.selected = [] ; 
		$scope.selectedName = [] ;
	}  
	$scope.isChecked = function(id){  
	        return $scope.selected.indexOf(id) >= 0 ;  
	} ;  
	      
	$scope.updateSelection = function($event,id,name){  
	    var checkbox = $event.target ;  
	    var checked = checkbox.checked ;  
	   if(checked){  
	       $scope.selected.push(id) ;  
	       $scope.selectedName.push(name);
	    }else{  
	        var idx = $scope.selected.indexOf(id) ;
	        var idy = $scope.selectedName.indexOf(name);
	         $scope.selected.splice(idx,1) ;  
	         $scope.selectedName.splice(idy,1) ;  
	     }  
	} ; 	
}];


//对应候选用户表单
var KisBpmcandidateUserPopupCtrl = ['$scope', '$modal', '$http', function($scope, $modal,$http) {
	$scope.pageSize = 10;
	var userId =document.URL.split('&userId=');
	$scope.search = function() {
		var searchparam=$scope.searchparam;
		var userurl = KISBPM.URL.getURL('getUsers?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
		 $http({method: 'GET', url:userurl }).
		 	success(function (data) {
		 			$scope.users=data;
		 			$scope.userpages = Math.ceil($scope.users.totalNumber / $scope.pageSize); //分页数
		 			$scope.usernewPages = $scope.userpages > 5 ? 5 : $scope.userpages;
		 			$scope.userpageList = [];
		 			$scope.userselPage = 1;
		 			$scope.selSort='user';
		 			for (var i = 0; i < $scope.usernewPages; i++) {
		 				$scope.userpageList.push(i + 1);
		 			};
		 	}).
		 		error(function (data) {
		 			console.log('查询用户报错');
		 });
	}
	$scope.setData = function (page ,keyword) {
		var firstNumber=(page-1)*10;
		var userurl = KISBPM.URL.getURL('getUsers?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
		$http({method: 'GET', url:userurl }).
	 	success(function (data) {
	 			$scope.users.items=data.items;
	 	}).
	 		error(function (data) {
	 			console.log('查询用户报错');
	 		});
		
	}
	
	$scope.selectPage = function (page) {
		//不能小于1大于最大
		if (page < 1 || page > $scope.userpages) return;
		//最多显示分页数5
		if (page > 2) {//因为只显示5个页数，大于2页开始分页转换
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.userpages ? $scope.userpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.userpageList = newpageList;
		}
		$scope.userselPage = page;
		var searchparam=$scope.searchparam;
		$scope.setData(page,searchparam);
		$scope.isActivePage(page);
	};
	$scope.isActivePage = function (page) {
			return $scope.userselPage == page ;
	};
	if($scope.candidateUsersNameValue!=''&&$scope.candidateUsersNameValue!=undefined){
		$scope.selected = $scope.candidateUsersValue.split(",") ; 
		$scope.selectedName = $scope.candidateUsersNameValue.split(",") ;
	}else{
		$scope.selected = [] ; 
		$scope.selectedName = [] ;
	}  
    $scope.isChecked = function(id){
        return $scope.selected.indexOf(id) >= 0 ;  
    } ;  
      
    $scope.updateSelection = function($event,id,last){  
        var checkbox = $event.target ;  
        var checked = checkbox.checked ;  
        if(checked){  
            $scope.selected.push(id) ;
            $scope.selectedName.push(last);
        }else{  
            var idx = $scope.selected.indexOf(id) ;
            var idy = $scope.selectedName.indexOf(last);
            $scope.selected.splice(idx,1) ;
            $scope.selectedName.splice(idy,1);
        } 
    } ;
	
	$scope.save = function() {
		$scope.candidateUsers($scope.selected,$scope.selectedName);
		$scope.$hide();
	}
		
	$scope.close = function() {
		$scope.$hide();
	}
	//上一页
	$scope.Previous = function () {
		$scope.selectPage($scope.userselPage - 1);
	}
	//下一页
	$scope.Next = function () {
		$scope.selectPage($scope.userselPage + 1);
	};
	
}];

