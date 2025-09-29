classDiagram
direction BT
class AuthControllerImpl
class AuthenticationFilter
class Booking
class BookingControllerImpl
class BookingCreateDto
class BookingDto
class BookingRepository {
<<Interface>>

}
class BookingServiceImpl
class BookingStatus {
<<enumeration>>

}
class ClassType
class ClassTypeControllerImpl
class ClassTypeDto
class ClassTypeRepository {
<<Interface>>

}
class ClassTypeServiceImpl
class Gym
class GymApplication
class GymControllerImpl
class GymCreateDto
class GymDto
class GymRepository {
<<Interface>>

}
class GymServiceImpl
class IAuthController {
<<Interface>>

}
class IBookingController {
<<Interface>>

}
class IBookingService {
<<Interface>>

}
class IClassTypeController {
<<Interface>>

}
class IClassTypeService {
<<Interface>>

}
class IGymController {
<<Interface>>

}
class IGymService {
<<Interface>>

}
class ISessionsController {
<<Interface>>

}
class ISessionsService {
<<Interface>>

}
class IUserController {
<<Interface>>

}
class IUserService {
<<Interface>>

}
class SecurityConfig
class SessionCreateDto
class SessionDto
class SessionServiceImpl
class SessionType {
<<enumeration>>

}
class Sessions
class SessionsControllerImpl
class SessionsRepository {
<<Interface>>

}
class User
class UserControllerImpl
class UserDto
class UserLoginDto
class UserRegisterDto
class UserRepository {
<<Interface>>

}
class UserServiceImpl
class UserType {
<<enumeration>>

}
class WorkoutType {
<<enumeration>>

}

AuthControllerImpl  ..>  IAuthController 
Booking "1" *--> "status 1" BookingStatus 
Booking "1" *--> "gym 1" Gym 
Booking "1" *--> "sessions 1" Sessions 
Booking "1" *--> "user 1" User 
BookingControllerImpl  ..>  IBookingController 
BookingServiceImpl  ..>  IBookingService 
ClassType "1" *--> "workoutType 1" WorkoutType 
ClassTypeControllerImpl  ..>  IClassTypeController 
ClassTypeServiceImpl  ..>  IClassTypeService 
Gym "1" *--> "classTypes *" ClassType 
GymControllerImpl  ..>  IGymController 
GymServiceImpl  ..>  IGymService 
SessionServiceImpl  ..>  ISessionsService 
Sessions "1" *--> "type 1" SessionType 
SessionsControllerImpl  ..>  ISessionsController 
User "1" *--> "userType 1" UserType 
UserControllerImpl  ..>  IUserController 
UserServiceImpl  ..>  IUserService 
