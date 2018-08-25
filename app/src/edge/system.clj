;; Copyright © 2016, JUXT LTD.

(ns edge.system
  "Components and their dependency relationships"
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [integrant.core :as ig]))

;; There will be integrant tags in our Aero configuration. We need to
;; let Aero know about them using this defmethod.
(defmethod aero/reader 'ig/ref [_ _ value]
  (ig/ref value))

(defn config
  "Read EDN config, with the given profile. See Aero docs at
  https://github.com/juxt/aero for details."
  [profile]
  (-> (io/resource "config.edn") ;; <1>
      (aero/read-config {:profile profile})) ;; <2>
  )

(defn system-config
  "Construct a new system, configured with the given profile"
  [profile]
  (let [config (config profile) ;; <1>
        system-config (:ig/system config)] ;; <2>
    (ig/load-namespaces system-config) ;; <3>
    system-config ;; <4>
    ))
