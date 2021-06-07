# Team Bazingaa Project

Program Language: 
Java

input data: 
1. User location (latitude and longitude) - Format: double, Length: 10 digits only
2. Dataset from Singapore Government Data
https://data.gov.sg/dataset/chas-clinics?resource_id=21dace06-c4d1-4128-9424-aba7668050dc
The dataset is .kml format, and transformed the format to .xlsx for the further manipulation.

Output data:
1. Clinic Location (latitude and longitude) - Format: double, Length: 10 digits only, will only display the closest place
2. Clinic Name - Format: String

Main Point:
1. The user can know the clinic name
2. The system can generate the detailed address based on Clinic Location (latitude and longitude)

API:
1. The API can receive the user location and generate the closest Clinic Location or Clinic Name

format 1: javaAPI/{latitude}/{longitude}
it will generate the closest Clinic Location

format 2: javaAPI/{latitude}/{longitude}/clinic
it will generate the closest Clinic Name

SOS function:
1. The user can click the SOS button to submit their location and receive extra help + know which clinic is available and closest for them at that moment

Remark:
The API is developed with Springboot
