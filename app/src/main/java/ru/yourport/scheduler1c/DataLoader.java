package ru.yourport.scheduler1c;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.util.Date;

public class DataLoader extends AsyncTask<String, Integer, String[][]>{

    private static final String NAMESPACE = "http://web/tfk/ExchangeTFK";
    private static final String URL = "https://kamaz.ddns.net:10100/testut/ws/ExchangeTFK.1cws";
    private static final String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:SayHello";
    private static final String METHOD_NAME = "Операция";
    private static final String LOG_TAG = "myLogs";
    private long TimeStart = 0, TimeEnd = 0;
    private String ERROR = "";

    public String getERROR() {
        return ERROR;
    }

    public long getTimeEnd() {
        return TimeEnd;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Date date = new Date();
        TimeStart = date.getTime();
    }

    @Override
    protected void onPostExecute(String[][] strings) {
        super.onPostExecute(strings);

    }

    @Override
    protected String[][] doInBackground(String... strings) {

        String[][] resultString = new String[0][0];
        String LOGIN = "wsChangeServis";//arg[0]
        String PASSWORD = "Service2018";//arg[1]
        //String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:Operation";
        //String METHOD_NAME = "Операция";//Выполнить Operation SayHello
        //Log.d(LOG_TAG, "Login: " + LOGIN);
        //Log.d(LOG_TAG, "Password: " + PASSWORD);

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Запрос", "СписокОрганизаций");
            //request.addProperty("id", sale.getId());
            //SimpleDateFormat dateFormat = new SimpleDateFormat(
            //        "yyyy-MM-dd'T'HH:mm:ss");
            //request.addProperty("date", dateFormat.format(sale.getDate()));
            //request.addProperty("clientCardNumber", sale.getCardNumber());
            //request.addProperty("bonuses", Double.toString(sale.getBonuses()));
            //...
            //
            // see - http://code.google.com/p/ksoap2-android/wiki/CodingTipsAndTricks#Adding_an_array_of_complex_objects_to_the_request
            //SoapObject sales = new SoapObject(NAMESPACE, "items");
            //for (SaleItemInformation item : sale.getSales()) {
            //    SoapObject itemSoap = new SoapObject(NAMESPACE, "Items");
            //    itemSoap.addProperty("Code", item.getItem().getSourceCode());
            //    itemSoap.addProperty("Quantity", Double.toString(item.getQuantity()));
            //    //...
            //    sales.addSoapObject(itemSoap);
            //}
            //request.addSoapObject(sales);

            //SoapObject query = new SoapObject(NAMESPACE, "ПолучитьСписокОбъектовЗапроса");
            //query.addProperty("Тип", "ЭлектроннаяОчередь");
            //request.addSoapObject(query);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER12);
            // Тоже важный элемент - не выводит типы данных в элементах xml
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            SSLConnection.allowAllSSL();

            HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(
                    URL, LOGIN, PASSWORD);
            androidHttpTransport.debug = true;

            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                //SoapObject resultsRequestSoap = (SoapObject) envelope.bodyIn;
                //System.out.println("Response::" + resultsRequestSoap.toString());
                //resultString = "Response::" + resultsRequestSoap.toString();
                String result = envelope.getResponse().toString();
                Log.d(LOG_TAG, "Result: " + result);

                JSONObject jsonObject = new JSONObject(result);
                JSONArray ja = jsonObject.getJSONArray("МассивОрганизаций");

                resultString = new String[ja.length()+1][3];

                resultString[0][1] = jsonObject.getString("Текст");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject joOrg = ja.getJSONObject(i);
                    String id = joOrg.getString("ID");
                    String name = joOrg.getString("Наименование");
                    int idfb = joOrg.getInt("IDFb");
                    Log.d(LOG_TAG, "ID = " + id + ", Наименование = " + name +
                            ", IDFb = " + idfb);

                    resultString[i+1][0] = id;
                    resultString[i+1][1] = name;
                }

            } catch (Exception e) {
                Log.d(LOG_TAG, e.getClass() + " HTTP TRANSPORT error: " + e.getMessage());
                ERROR = e.getMessage();

                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getClass() + " error: " + e.getMessage());
            ERROR = e.getMessage();
            e.printStackTrace();
        }

        int index401 = ERROR.indexOf("HTTP status: 401");
        int indexHost = ERROR.indexOf("Unable to resolve host");
        if (index401 > -1) {
            ERROR = "Не пройдена авторизация";
            Log.d(LOG_TAG, ERROR);
        } else if (indexHost > -1) {
            ERROR = "Не найден хост";
            Log.d(LOG_TAG, ERROR);
        }

        //System.out.println(resultString);
        Date date = new Date();
        TimeEnd = date.getTime() - TimeStart;

        Log.d(LOG_TAG, "Результат времени в милисекундах: " + TimeEnd);

        return resultString;
    }
}