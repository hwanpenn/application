
var KisBpmCopyUsersCtrl = [ '$scope', '$modal', '$timeout', '$translate', function($scope, $modal, $timeout, $translate) {
		var opts = {
		        template:  'editor-app/configuration/properties/copyUser-popup.html?version=' + Date.now(),
		        scope: $scope
		    };
		 $modal(opts);
	 $scope.copyUsers=function(data,namevalue){
	    	$scope.copyUsersValue=data.toString().split(",");
	    	$scope.copyUsersNamevalue=namevalue.toString().split(",");
	    	$scope.property.namevuales=namevalue.toString();
	    	$scope.copyusers={};
	    	if($scope.copyUsersValue!=''){
	    		$scope.copyusers.copyUsersArray=[];
	    	for(var i = 0; i < $scope.copyUsersValue.length; i++){
    			var Users={value:$scope.copyUsersValue[i],name:$scope.copyUsersNamevalue[i]};
    			$scope.copyusers.copyUsersArray.push(Users);
    			}
	    	}else{
	    		$scope.copyusers.copyUsersArray=undefined;
	    	}
	    	$scope.property.value = {};
	    	$scope.property.value.copyusers=$scope.copyusers;
	    	 $scope.updatePropertyInModel($scope.property);
	    }
}];


//对应候选用户表单
var KisBpmCopyUserPopupCtrl = ['$scope', '$modal', '$http', function($scope, $modal,$http) {
	   if ($scope.property.value !== undefined && $scope.property.value !== null
		        && $scope.property.value.copyusers !== undefined
		        && $scope.property.value.copyusers !== null) 
		    {
		        $scope.copyusers = $scope.property.value.copyusers;
		    } else {
		        $scope.copyusers = {};
		    }
	    if ($scope.copyusers.copyUsersArray == undefined || $scope.copyusers.copyUsersArray.length == 0)
	    {
	    	$scope.copyusers.copyUsersArray = [];
	    	$scope.copyUsersValue='';
	    }else{
	    	var tmpcopyusersUsers=[];
	    	var tmpcopyusersUsersName=[];
	    	for(var i = 0; i < $scope.copyusers.copyUsersArray.length; i++){
	    		if(!$scope.copyusers.copyUsersArray[i].name==''){
	    			tmpcopyusersUsers.push($scope.copyusers.copyUsersArray[i].value);
	    			tmpcopyusersUsersName.push($scope.copyusers.copyUsersArray[i].name);
	    		}
			}
	    	$scope.copyUsersValue=tmpcopyusersUsers.toString();
	    	$scope.copyUsersNameValue=tmpcopyusersUsersName.toString();
	    }
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
	
	if($scope.copyUsersValue!=''){
		$scope.selected = $scope.copyUsersValue.split(",") ; 
		$scope.selectedName = $scope.copyUsersNameValue.split(",") ;
	}else{
		$scope.selected = [] ; 
		$scope.selectedName = [] ;
	}  
		$scope.copyusersname=$scope.selectedName.toString();
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
        $scope.copyusersname=$scope.selectedName.toString();
    } ;
	
	$scope.save = function() {
		$scope.copyUsers($scope.selected,$scope.selectedName);
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
