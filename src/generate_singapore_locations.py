import json
import requests
import pandas as pd


def get_locations_coordinates(data_df):

    """
    This function returns a set of coordinates with addresses of found in Singapore public housing data set through API.
    Inspiration from https://github.com/yuan-yexi/hdb-resale-api/blob/master/script.py
    :param data_df: housing data that has has attributes as listed in first for loop below
    :return: dataframe of addresses with coordinates and other attributes
    """

    town = []
    flat_type = []
    flat_model = []
    floor_area_sqm = []
    street_name = []
    resale_price = []
    month = []
    remaining_lease = []
    lease_commence_date = []
    storey_range = []
    _id = []
    block = []
    for i in range(0, len(data_df)):
        town.append(data_df[i]['town'])
        flat_type.append(data_df[i]['flat_type'])
        flat_model.append(data_df[i]['flat_model'])
        floor_area_sqm.append(data_df[i]['floor_area_sqm'])
        street_name.append(data_df[i]['street_name'])
        resale_price.append(data_df[i]['resale_price'])
        month.append(data_df[i]['month'])
        remaining_lease.append(data_df[i]['remaining_lease'])
        lease_commence_date.append(data_df[i]['lease_commence_date'])
        storey_range.append(data_df[i]['storey_range'])
        _id.append(data_df[i]['_id'])
        block.append(data_df[i]['block'])
    
    
    df_hdb_price = pd.DataFrame({
        'town': town,
        'flat_type': flat_type,
        'flat_model': flat_model,
        'floor_area_sqm': floor_area_sqm,
        'street_name': street_name,
        'resale_price': resale_price,
        'month': month,
        'remaining_lease': remaining_lease,
        'lease_commence_date': lease_commence_date,
        'storey_range': storey_range,
        '_id': _id,
        'block': block
    })
    
    address_list = [x for x in df_hdb_price['street_name']]
    
    # This may take a while...
    latitude = []
    longitude = []
    blk_no = []
    road_name = []
    postal_code = []
    address = []
    count = 0
    for row in range(len(address_list)):
        #formulate query string
        query_address = address_list[row]
        # Query data from Resale API of all transactions from Jan 2017 onward
        query_string='https://developers.onemap.sg/commonapi/search?searchVal='+str(query_address)+'&returnGeom=Y&getAddrDetails=Y'
        resp = requests.get(query_string)
        #Convert JSON into Python Object
        data_geo_location=json.loads(resp.content)
        if data_geo_location['found'] != 0:
            latitude.append(data_geo_location['results'][0]['LATITUDE'])
            longitude.append(data_geo_location['results'][0]['LONGITUDE'])
            blk_no.append(data_geo_location['results'][0]['BLK_NO'])
            road_name.append(data_geo_location['results'][0]['ROAD_NAME'])
            postal_code.append(data_geo_location['results'][0]['POSTAL'])
            address.append(query_address)
            # print(str(query_address) + " ,Lat: " + data_geo_location['results'][0]['LATITUDE'] + " Long: " + data_geo_location['results'][0]['LONGITUDE'])
        else:
            print("No Results")
    
    df_coordinates = pd.DataFrame({
        'latitude': latitude,
        'longitude': longitude,
        'blk_no': blk_no,
        'road_name': road_name,
        'postal_code': postal_code,
        'address': address
    })
    len(df_coordinates)


if __name__=="__main__":

    query_string = 'https://data.gov.sg/api/action/datastore_search?resource_id=42ff9cfe-abe5-4b54-beda-c88f9bb438ee&limit=500'
    resp = requests.get(query_string)
    # Convert JSON into Python Object
    data = json.loads(resp.content)

    len(data['result']['records'])

    hdb_price_dict_records = data['result']['records'].copy()

    data_df_coordinates = get_locations_coordinates(hdb_price_dict_records)

    data_df_coordinates.to_csv("./data/sample_locations.csv")

    # === API Information === #
    # https://data.gov.sg/dataset/resale-flat-prices
    # Resale Flat Prices on Data.gov.sg provides the historical transactions from 1990 onwards.
    # https://docs.onemap.sg/
    # A robust API that provides data on Singapore-specific location coordinates, transport, routing, demographic data and much more.
    # We will be leveraging this API to obtain the Geo-coordinates needed for this exercise.