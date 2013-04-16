(ns task-manager.data
  (:use [datomic.api :only [q db] :as d]))

(def uri "datomic:mem://tasks")
(d/create-database uri)
(def conn (d/connect uri))
(defn dbval [] (db conn))

(def schema-tx (read-string (slurp "resources/schema.edn")))
(d/transact conn schema-tx)

(def seed-tx (read-string (slurp "resources/seed.dtm")))
(d/transact conn seed-tx)

(defn tasks []
  (let [results (q '[:find ?e :where [?e :task/number]] (dbval))]
    (map (fn [eid]
           (let [ent (d/entity (dbval) (first eid))]
             (d/touch ent)))
         results)))

(defn task! [num desc]
  (let [tx [{:db/id #db/id [:db.part/user]
             :task/description desc
             :task/number num}]]
    (d/transact conn tx)))

(defn create-task! [desc]
  (d/transact conn [[:create-task desc]]))
