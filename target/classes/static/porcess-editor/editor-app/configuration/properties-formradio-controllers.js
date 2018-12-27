
var KisBpmRadioPropertyCtrl = [ '$scope', '$modal', '$timeout', '$translate', function($scope, $modal, $timeout, $translate) {
		var opts = {
		        template:  'editor-app/configuration/properties/formkeydefinition-popup.html?version=' + Date.now(),
		        scope: $scope
		    };
		    $modal(opts);
		    
	 $scope.candidateForm=function(data){
	    	$scope.property.value=data.toString();
	    	 $scope.updatePropertyInModel($scope.property);
	    }
}];


var KisBpmformkeydefinitionPopupCtrl=['$scope', '$modal', '$http', function($scope, $modal,$http) {
	$scope.pageSize = 10;
	var userId =document.URL.split('&userId=');
	$scope.search = function() {
		var searchparam=$scope.searchparam;
		var formurl = KISBPM.URL.getURL('forms?keyword='+searchparam+'&firstNumber=0&pageSize=10&userId='+userId[1]);
		 $http({method: 'GET', url:formurl }).
		 	success(function (data) {
		 			$scope.forms=data;
		 			$scope.formpages = Math.ceil($scope.forms.totalNumber / $scope.pageSize); //分页数
		 			$scope.formnewPages = $scope.formpages > 5 ? 5 : $scope.formpages;
		 			$scope.formpageList = [];
		 			$scope.formselPage = 1;
		 			$scope.currentPage=1;
		 			for (var i = 0; i < $scope.formnewPages; i++) {
		 				$scope.formpageList.push(i + 1);
		 			};
		 	}).
		 		error(function (data) {
		 			console.log('查询表单报错');
		 });
	}
	$scope.setData = function (page ,keyword) {
		var firstNumber=(page-1)*10;
		var formurl = KISBPM.URL.getURL('forms?keyword='+keyword+'&firstNumber='+firstNumber+'&pageSize=10&userId='+userId[1]);
		$http({method: 'GET', url:formurl }).
	 	success(function (data) {
	 			$scope.forms.items=data.items;
	 	}).
	 		error(function (data) {
	 			console.log('查询用户报错');
	 		});
		
	}
	
	$scope.selectPage = function (page) {
		//不能小于1大于最大
		if (page < 1 || page > $scope.formpages) return;
		//最多显示分页数5
		if (page > 2) {//因为只显示5个页数，大于2页开始分页转换
				var newpageList = [];
				for (var i = (page - 3) ; i < ((page + 2) > $scope.formpages ? $scope.formpages : (page + 2)) ; i++) {
				newpageList.push(i + 1);
				}
				$scope.formpageList = newpageList;
		}
		$scope.formselPage = page;
		var searchparam=$scope.searchparam;
		$scope.setData(page,searchparam);
		$scope.isActivePage(page);
	};
	$scope.isActivePage = function (page) {
			return $scope.formselPage == page ;
	};
	
	
	$scope.selected = [] ;  
	$scope.selectedname = [] ;
    $scope.isChecked = function(id){  
        return $scope.selected.indexOf(id) >= 0 ;  
    } ;  
      
    $scope.updateSelection = function($event,id,last){  
    	$scope.selected = [] ;  
    	$scope.selectedname = [] ;
        var checkbox = $event.target ;  
        var checked = checkbox.checked ;  
        if(checked){  
            $scope.selected.push(id) ;
            $scope.selectedname.push(last);
        }else{  
            var idx = $scope.selected.indexOf(id) ;  
            $scope.selected.splice(idx,1) ;  
        }  
    } ;
	
	$scope.save = function() {
		$scope.candidateForm($scope.selected);
		/*	$scope.candidateformsName($scope.selectedname);*/
		$scope.$hide();
	}
		
	$scope.close = function() {
		$scope.$hide();
	}
	//上一页
	$scope.Previous = function () {
		$scope.selectPage($scope.formselPage - 1);
	}
	//下一页
	$scope.Next = function () {
		$scope.selectPage($scope.formselPage + 1);
	};
	
}];