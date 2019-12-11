package ru.yourport.scheduler1c;

import android.os.AsyncTask;
import android.util.Log;

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
        String LOGIN = "wsChangeServis";
        String PASSWORD = "Service2018";
        String query = strings[0];
        //String SOAP_ACTION = "http://web/tfk/ExchangeTFK#ExchangeTFK:Operation";
        //String METHOD_NAME = "Операция";//Выполнить Operation SayHello
        //Log.d(LOG_TAG, "Login: " + LOGIN);
        //Log.d(LOG_TAG, "Password: " + PASSWORD);

        try {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("Запрос", query);//"СписокОрганизаций"
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

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER12);
            // Тоже важный элемент - не выводит типы данных в элементах xml
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);

            SSLConnection.allowAllSSL();

            HttpTransportBasicAuthSE androidHttpTransport = new HttpTransportBasicAuthSE(
                    URL, LOGIN, PASSWORD);
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);
            //SoapObject resultsRequestSoap = (SoapObject) envelope.bodyIn;
            //resultString = "Response::" + resultsRequestSoap.toString();
            String result = envelope.getResponse().toString();
            Log.d(LOG_TAG, "Result: " + result);

            try {
                JsonParser jsonParser = new JsonParser();
                resultString = jsonParser.Parser(result);
            } catch (Exception e) {
                ERROR = e.getMessage();
                Log.d(LOG_TAG, e.getClass() + " JSONObject error: " + ERROR);
                e.printStackTrace();
            }
        } catch (Exception e) {
            ERROR = e.getMessage();
            Log.d(LOG_TAG, e.getClass() + " error: " + ERROR);
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

        Date date = new Date();
        TimeEnd = date.getTime() - TimeStart;

        Log.d(LOG_TAG, "Результат времени в милисекундах: " + TimeEnd);

        return resultString;
    }
}