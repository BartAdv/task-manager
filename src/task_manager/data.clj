(ns task-manager.data
  (:use [datomic.api :only [q db] :as d])
  (:require clojure.java.io))

(def uri "datomic:mem://tasks")
(d/create-database uri)
(def conn (d/connect uri))
(defn dbval [] (db conn))

(def schema-tx (read-string (slurp (clojure.java.io/resource "schema.edn"))))
(d/transact conn schema-tx)

(def seed-tx (read-string (slurp (clojure.java.io/resource "seed.dtm"))))
(d/transact conn seed-tx)

(defn task [num]
  (let [[eid] (first (q '[:find ?e :in $ ?num :where [?e :task/number ?num]] (dbval) num))] 
    (d/entity (dbval) eid)))

(defn tasks []
  (->>
   (q '[:find ?e :where [?e :task/number]] (dbval))
   (map #(d/entity (dbval) (first %)))))

(defn create-task [desc]
  (d/transact conn [[:create-task desc]]))

(defn update-task [number & attribs]
  (let [tx [(merge
              {:db/id #db/id[:db.part/user] :task/number number} 
              (apply hash-map attribs))]]
    (d/transact conn tx)))

(defn create-comment [id text]
  (let [tx [{:db/id (d/tempid :db.part/user) 
             :comment/text text 
             :_comments id }]]
    (d/transact conn tx)))

(defn remove-comment [cid]
  (d/transact conn [[:db.fn/retractEntity cid]]))


