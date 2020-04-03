angular.module('app', ['ngResource', 'ngRoute'])
    .config(function ($routeProvider, $httpProvider) {
        $routeProvider
            .when("/register", {
                templateUrl: "partials/register.html",
                controller: "RegisterController",
                controllerAs: "rCtrl"
            })
            .when("/login", {
                templateUrl: "partials/login.html",
                controller: "LoginController",
                controllerAs: "lCtrl"
            })
            .when("/secured", {
                templateUrl: "partials/secured.html"
            })
            .when("/addMaterial", {
                templateUrl: "partials/addMaterial.html",
                controller: "AddMaterialController",
                controllerAs: "amCtrl"
            })
            .when("/materialList", {
                templateUrl: "partials/materialList.html",
                controller: "MaterialListController",
                controllerAs: "listCtrl"
            })
            .when("/details/:index", {
                templateUrl: "partials/details.html",
                controller: "DetailsController",
                controllerAs: "dCtrl"
            })
            .otherwise({
                redirectTo: "/"
            });

       // $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest'


    })
    .constant('ENDPOINT', '/api/users')
    .constant('LOGIN', 'login')
    .constant('LOGOUT', 'logout')
    .constant('MAT_ENDPOINT', '/api/materials/:index')
    .factory('UserFactory', function ($resource, ENDPOINT) {
        return $resource(ENDPOINT)
    })
    .factory('MaterialFactory',function($resource,MAT_ENDPOINT){
        return $resource(MAT_ENDPOINT)
    })
    .service('MaterialService',function(MaterialFactory){
        var service = this;
        service.getAll = function(){
            return MaterialFactory.query();
        }
        service.getOne = function (id) {
            return MaterialFactory.get({index:id})

        }
        service.addMaterial = function(material){
            material.$save();
        }

    })
    .service('UserService', function (UserFactory) {
        var service = this;

        service.addUser = function (user) {
            user.$save();
        }


    })
    .controller('RegisterController', function (UserFactory, UserService) {
        var ctrl = this;
        ctrl.user = new UserFactory();
        ctrl.addUser = function () {
            UserService.addUser(ctrl.user);
            ctrl.user = new UserFactory();
        }

    })
    .service('AuthService', function ($http, LOGIN, LOGOUT) {
        var service = this;
        service.authenticate = function (userdata, loginSuccess) {
            var authHeader = {Authorization: 'Basic ' + btoa(userdata.username + ':' + userdata.password)}
            var config = {headers: authHeader}
            $http
                .post(LOGIN, {}, config)
                .then(function success(value) {
                    loginSuccess()
                }, function error(response) {
                    console.log(response.status)
                })
        }

        service.logout = function (logoutSucess) {
            $http
                .post(LOGOUT)
                .then(logoutSucess())
        }

    })
    .controller('LoginController', function ($rootScope, $location, AuthService) {
        var ctrl = this
        ctrl.user = {}

        var loginSucess = function () {
            $rootScope.authenticated = true
            $location.path("/secured")
        }

        var logoutSucess = function () {
            $rootScope.authenticated = false
            $location.path("/")

        }

        ctrl.login = function () {
            AuthService.authenticate(ctrl.user, loginSucess)
        }
        ctrl.logout = function () {
            AuthService.logout(logoutSucess())
        }

    })
    .controller("AddMaterialController",function (MaterialService,MaterialFactory) {
        var ctrl=this;
        ctrl.material= new MaterialFactory();
        ctrl.add = function(){
            MaterialService.addMaterial(ctrl.material)
            ctrl.material = new MaterialFactory()
        }

    })
    .controller("MaterialListController",function(MaterialService){
        var ctrl = this
        ctrl.materials = MaterialService.getAll();
    })
    .controller('DetailsController',function(MaterialService,$routeParams){
        var ctrl = this
        var index = $routeParams.index;
        ctrl.material = MaterialService.getOne(index);
    })


