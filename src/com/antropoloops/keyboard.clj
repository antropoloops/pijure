(ns com.antropoloops.keyboard
  (:require [com.stuartsierra.component :as component]
            [com.antropoloops.osc :as osc])
  (:import [org.jnativehook GlobalScreen NativeHookException]
           [org.jnativehook.keyboard NativeKeyEvent NativeKeyListener]
           [java.util.logging Logger Level]))






(defn listener [osc]
  (reify NativeKeyListener
    (nativeKeyPressed [this e]
      
      (println :native-key-pressed  (NativeKeyEvent/getKeyText (.getKeyCode e)))
      (when  (= "5" (NativeKeyEvent/getKeyText (.getKeyCode e)))
        (osc/send-message osc osc/path 5)) 
      
      )
    (nativeKeyReleased [this e]
      #_(println :native-key-released (NativeKeyEvent/getKeyText (.getKeyCode e))))
    (nativeKeyTyped [this e]
      #_(println :native-key-typed (NativeKeyEvent/getKeyText (.getKeyCode e)))))) 


(defrecord KeyboardListener []
  component/Lifecycle
  (start [component]
    (.setLevel (Logger/getLogger "org.jnativehook") Level/WARNING)    
    (println ";; Starting keyboard listener")
    (try
      (let [listener* (listener (:osc-server component))]
        (GlobalScreen/registerNativeHook)
        (GlobalScreen/addNativeKeyListener listener*)
        (assoc component :listener listener*))
      (catch Exception e (do
                           (println e)
                           component))))

  (stop [component]
    (println ";; Stopping keyboard listener")
    (try
      (GlobalScreen/removeNativeKeyListener (:listener component))
      (GlobalScreen/unregisterNativeHook)
      (catch Exception e (println e)))
    component))

(defn new* []
  (KeyboardListener.))
