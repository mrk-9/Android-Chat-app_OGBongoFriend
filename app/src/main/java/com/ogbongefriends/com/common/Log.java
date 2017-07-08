
package com.ogbongefriends.com.common;


/**
 * @author vivek, Ash
 */

//

public final class Log {
    
    private static boolean _logEnabled = true;
    public final static String tag = "ash"; 
    
    //
    public static boolean enabled(){ return _logEnabled;} 
    
    //
    public static void content(String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.i(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    //
    public static void i(String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.i(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    //
    public static void i(Exception e){
        
        if(_logEnabled)
            e.printStackTrace();
    }
    
    //
    public static void v(String tag, String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.v(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    //
    public static void d(String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.d(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    //
    public static void d(String tag, String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.d(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    //
    public static void e(String tag, String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.e(tag, message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    public static void syncMsg(String message){
        
        try{
            
            if(_logEnabled)
                android.util.Log.e("ash_sync", message);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    //
    public static void e(String tag, String message, Throwable t){
        
        try{
            
            if(_logEnabled)
                android.util.Log.e(tag, message, t);
            
        }catch(Exception e){e.printStackTrace();}
    }
    
    //
    public static void ex(Exception e){
        
        if(_logEnabled)
            e.printStackTrace();
    }
}
