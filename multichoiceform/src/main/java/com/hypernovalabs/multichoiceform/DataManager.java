package com.hypernovalabs.multichoiceform;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.hypernovalabs.multichoiceform.config.MCFOptionsConfig;

/**
 * Created by celso on 2/21/19
 */
public class DataManager {

    private static DataManager instance = null;
    private static final String DEFAULT_PROTECTED_PREFS = "MCF_PROTECTED_PREFS";
    private static final String COMPLETE_DATA = "MCF_COMPLETE_DATA";

    private Context mContext;
    private SharedPreferences mProtectedSettings;
    //    private OnRetrieveDataFinishListener mCallback;
    private AsyncTask<Void, Integer, MCFOptionsConfig> mRetrieveTask;
    private AsyncTask<MCFOptionsConfig, Void, Boolean> mSaveTask;
    private boolean isRetrievingData;
    private boolean isSavingData;

    private DataManager() {
    }

    private static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static void setBaseContext(Context ctx) {
        getInstance().setContext(ctx);
    }

    private void setContext(Context ctx) {
        mContext = ctx;
    }

    public static Context getBaseContext() {
        return getInstance().mContext;
    }

    private SharedPreferences getProtectedSetting() {
        if (mProtectedSettings == null) {
            mProtectedSettings = mContext.getSharedPreferences(DEFAULT_PROTECTED_PREFS, Context.MODE_PRIVATE);
        }
        return mProtectedSettings;
    }


    public static void clearData() {
        clearAll();
        getInstance().mContext = null;
        instance = null;
    }

    private static void clearAll() {
        SharedPreferences.Editor editor = getInstance().getProtectedSetting().edit();
        editor.clear();
        editor.apply();
    }

    /*private MCFOptionsConfig getData() {
        String stringArray = instance.getProtectedSetting().getString(COMPLETE_DATA, null);

        if (stringArray != null) {
            String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
            byte[] bytes = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                bytes[i] = Byte.parseByte(split[i]);
            }
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0); // This is extremely important!
            MCFOptionsConfig model = (MCFOptionsConfig) parcel.readValue(MCFOptionsConfig.class.getClassLoader());
            parcel.recycle();
            return model;
        } else {
            return null;
        }
    }*/

    /*static void retrieveData() {
        if (getInstance().mRetrieveTask == null) {
            if (!getInstance().isSavingData) {
                getInstance().mRetrieveTask = new AsyncTask<Void, Integer, MCFOptionsConfig>() {
                    @Override
                    protected MCFOptionsConfig doInBackground(Void... params) {
                        try {
                            Log.e("Retrieve", "Started");
                            String stringArray = instance.getProtectedSetting().getString(COMPLETE_DATA, null);
                            clearAll();
                            Log.e("Retrieve", "Clear");
                            if (stringArray != null) {
                                byte[] bytes = Base64.decode(stringArray, Base64.DEFAULT);
                                Log.e("Bytes", bytes.length + "");
                                Parcel parcel = Parcel.obtain();
                                parcel.unmarshall(bytes, 0, bytes.length);
                                parcel.setDataPosition(0); // This is extremely important!
                                MCFOptionsConfig model = (MCFOptionsConfig) parcel.readValue(MCFOptionsConfig.class.getClassLoader());
                                parcel.recycle();
                                Log.e("Retrieve", "Finished");


                                ------------- Intento de pasar data a través de SharedPreferences

                                Log.e("Retrieve", "Started");
                                String stringArray = instance.getProtectedSetting().getString(COMPLETE_DATA, null);
                                clearAll();
                                Log.e("Retrieve", "Clear");
                                if (stringArray != null) {
                                    String substring = stringArray.substring(1, stringArray.length() - 1);
                                    Log.e("Retrieve", "Substringed");
                                    stringArray = null;
                                    String[] split = substring.split(", ");
                                    Log.e("Retrieve", "Splitted");
                                    byte[] bytes = new byte[split.length];
                                    Log.e("Bytes", bytes.length + "");
                                    for (int i = 0; i < split.length; i++) {
                                        if (isCancelled()) {
                                            return null;
                                        } else {
                                            bytes[i] = Byte.parseByte(split[i]);
                                        }
                                    }
                                    Parcel parcel = Parcel.obtain();
                                    parcel.unmarshall(bytes, 0, bytes.length);
                                    parcel.setDataPosition(0); // This is extremely important!
                                    MCFOptionsConfig model = (MCFOptionsConfig) parcel.readValue(MCFOptionsConfig.class.getClassLoader());
                                    parcel.recycle();
                                    Log.e("Retrieve", "Finished");
                                return model;
                            } else {
                                return null;
                            }
                        } catch (Exception e) {
                            return null;
                        }
                    }

                    @Override
                    protected void onCancelled() {
                        super.onCancelled();
                        getInstance().isRetrievingData = false;
                    }

                    @Override
                    protected void onPostExecute(MCFOptionsConfig data) {
                        super.onPostExecute(data);
                        getInstance().isRetrievingData = false;
                        if (getInstance().mCallback != null) {
                            getInstance().mCallback.OnRetrieveDataFinished(data != null, data);
                        }
                    }
                }.execute();
            }
            getInstance().isRetrievingData = true;
        } else {
            getInstance().isRetrievingData = false;
            getInstance().mRetrieveTask.cancel(true);
            getInstance().mRetrieveTask = null;
            retrieveData();
        }

    }

    static void cancelRequest() {
        getInstance().isRetrievingData = false;
        getInstance().isSavingData = false;
        if (getInstance().mRetrieveTask != null) {
            getInstance().mRetrieveTask.cancel(true);
            getInstance().mRetrieveTask = null;
        }
        if (getInstance().mSaveTask != null) {
            getInstance().mSaveTask.cancel(true);
            getInstance().mSaveTask = null;
        }
    }

    static void saveData(MCFOptionsConfig model) {

        if (getInstance().mSaveTask == null) {
            getInstance().mSaveTask = new AsyncTask<MCFOptionsConfig, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(MCFOptionsConfig... params) {
                    boolean isItRight;
                    try {
                        Log.e("Save", "Started");
                        Parcel parcel = Parcel.obtain();
                        parcel.writeValue(params[0]);
                        byte[] bytes = parcel.marshall();
                        Log.e("Bytes", bytes.length + "");
                        SharedPreferences.Editor editor = instance.getProtectedSetting().edit();
                        editor.putString(COMPLETE_DATA, Base64.encodeToString(bytes, Base64.DEFAULT));
                        bytes = null;
                        editor.apply();
                        parcel.recycle();
                        editor = null;
                        params = null;
                        isItRight = true;
                        Log.e("Save", "Finished");



                        *//*Log.e("Save", "Started");
                        Parcel parcel = Parcel.obtain();
                        parcel.writeValue(params[0]);
                        byte[] bytes = parcel.marshall();
                        Log.e("Bytes", bytes.length + "");
                        SharedPreferences.Editor editor = instance.getProtectedSetting().edit();
                        editor.putString(COMPLETE_DATA, Arrays.toString(bytes));
                        bytes = null;
                        editor.apply();
                        parcel.recycle();
                        editor = null;
                        params = null;
                        isItRight = true;
                        Log.e("Save", "Finished");*//*
                    } catch (Exception e) {
                        isItRight = false;
                    }
                    return isItRight;
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                    getInstance().isSavingData = false;
                }

                @Override
                protected void onPostExecute(Boolean isItRight) {
                    super.onPostExecute(isItRight);
                    getInstance().isSavingData = false;
                    if (getInstance().isRetrievingData) {
                        retrieveData();
                    }
                }
            }.execute(model);
            getInstance().isSavingData = true;
        } else {
            getInstance().isSavingData = false;
            getInstance().mSaveTask.cancel(true);
            getInstance().mSaveTask = null;
            retrieveData();
        }
    }

    static void setOnRequestFinishListener(OnRetrieveDataFinishListener listener) {
        if (getInstance().mRetrieveTask != null) getInstance().mRetrieveTask.cancel(true);
        getInstance().mCallback = listener;
    }

    interface OnRetrieveDataFinishListener {
        void OnRetrieveDataFinished(boolean isItRight, MCFOptionsConfig data);
    }*/
}