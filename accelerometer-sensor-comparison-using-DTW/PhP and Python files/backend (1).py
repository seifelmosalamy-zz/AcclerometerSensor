import numpy as np
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean
import pymysql
from pymysql import Error

try:
    connection = pymysql.connect(host='localhost', database='mostafa123', user='root',password='')

    cursor = connection.cursor()

    app_wave = []
    result = ""
    shortest_path = ""

       # read data entered

    cursor.execute("select count(*) from test_data where tested = 0;")
    test_data_rows = cursor.fetchone()

    test_data_rows = str(test_data_rows)
    test_data_rows = test_data_rows.replace(',', '')
    test_data_rows = test_data_rows.replace('(', '')
    test_data_rows = test_data_rows.replace(')', '')

    j=0
    while(j < int(test_data_rows)):
        cursor.execute("select id, xaxis, yaxis from test_data where tested = 0;")
        values = cursor.fetchone()

        id, xaxis, yaxis = values
        values = [xaxis, yaxis]
        app_wave.append(values)

        cursor.execute("UPDATE test_data SET tested = 1 where id = " + str(id) + ';')
        connection.commit()
        j += 1


    cursor.execute("select count(*) from temp;")
    rows = cursor.fetchone()

    rows = str(rows)
    rows = rows.replace(',', '')
    rows = rows.replace('(', '')
    rows = rows.replace(')', '')
    print("rows result - ", rows)
    row_num = int(rows.replace(',', ''))

    i = 1
    while (i <= row_num):

        cursor.execute("select file_path from temp where id = " + str(i) + ";")
        o_path = cursor.fetchone()

        o_path = str(o_path)
        o_path = o_path.replace(',', '')
        o_path = o_path.replace('(', '')
        o_path = o_path.replace(')', '')
        o_path = o_path.replace("'", '')

        print(o_path)
        dataset_wave = []

        f = open(o_path, "r")
        for x in f:
            line = x.split()

            arr = []
            arr.append(line[3])
            arr.append(line[4])
            dataset_wave.append(arr)

        dataset_wavee = np.array(dataset_wave)
        app_wavee = np.array(app_wave)
        distance, path = fastdtw(dataset_wavee, app_wavee, dist=euclidean)
        i+=1

        if(result == ""):
            result = distance
            shortest_path = o_path
        elif(result > distance):
            result = distance
            shortest_path = o_path

        print(shortest_path)
        print(result)

    cursor.execute("INSERT INTO `result_data`(`shape_number`) VALUES ('" + shortest_path + "');")
    connection.commit()
    print('DONE')


    # know the which shape and insert to database


except Error as e :
    print ("Error while connecting to MySQL", e)
