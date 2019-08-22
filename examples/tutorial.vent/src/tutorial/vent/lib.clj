(ns tutorial.vent.lib
  (:require
    [tutorial.vent.db :as db]))

(defn favorites
  []
  (filter #(true? (:favorite? %1)) (all)))

(defn all
  []
  (let [data (db/read)
        vents (:vents data)
        users (:users data)
        get-author (fn [vent] (let [username (get vent :username)
                                    user (get users username)]
                                user))
        add-author-to-vent (fn [vent] (assoc vent :author (get-author vent)))
        user-annotated-vents (map add-author-to-vent vents)]
    user-annotated-vents))

(defn followers
  [{:keys [username]}]
  {"john_smith"
   {:name "John Smith"}
   "jane_smith"
   {:name "Jane Smith"
    :following? true}})

(defn following
  [{:keys [username]}]
  (let [data (db/read)
        users (:users data)
        me (get users username)
        followed-usernames (:follows me)
        followed-users (select-keys users followed-usernames)
        set-following (fn [[k v]] [k (assoc v :following? true)])]
    (into {} (map set-following followed-users))))

(defn toggle-favorite
  [{:keys [vent-id]}]
  (println "toggling favorite on" vent-id))

(defn- generate-id
  []
  (str (java.util.UUID/randomUUID)))

(defn add-vent
  [{:keys [text username]}]
  (db/transact (let [data (db/read)
                     new-vent {:id (generate-id)
                               :username username
                               :text text}
                     new-vents (conj (:vents data) new-vent)
                     new-data (assoc data :vents new-vents)]
                 (db/store new-data))))

(defn toggle-follow
  [{:keys [to-follow username]}]
  (println username "is follwoing or unfollowing" to-follow))
