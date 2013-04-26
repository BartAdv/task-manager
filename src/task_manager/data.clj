(ns task-manager.data
  (:use [datomic.api :only [q db] :as d])
  (:require clojure.java.io))

(def uri "datomic:mem://tasks")
(d/create-database uri)
(def conn (d/connect uri))

(def schema-tx (read-string (slurp (clojure.java.io/resource "schema.edn"))))
(d/transact conn schema-tx)

(def seed-tx (read-string (slurp (clojure.java.io/resource "seed.dtm"))))
(d/transact conn seed-tx)

(defn load-db
  ([] (db conn))
  ([future] (get :db-after future)))

(defn save [& transactions]
  (d/transact conn transactions))

(defn get-entity [dbval id]
  (d/entity dbval id))

(defn get-task [dbval num]
  (let [[eid] (first (q '[:find ?e :in $ ?num :where [?e :task/number ?num]] dbval num))]
    (d/entity dbval eid)))

(defn tasks [dbval]
  (->>
   (q '[:find ?e :where [?e :task/number]] dbval)
   (map #(d/entity dbval (first %)))))

(defn create-task [desc]
  (let [tempid (d/tempid :db.part/user)
        tx [[:create-task desc tempid]]
        res @(d/transact conn tx)
        db (:db-after res)
        id (d/resolve-tempid db (:tempids res) tempid)]
    (d/entity db id)))

(defn update-task [number & attribs]
  (let [tempid (d/tempid :db.part/user)
        tx [(merge {:db/id tempid :task/number number}
                   (apply hash-map attribs))]
        res @(d/transact conn tx)
        db (:db-after res)
        id (d/resolve-tempid db (:tempids res) tempid)]
    (d/entity db id)))

(defn create-comment [tid text]
  (let [tempid (d/tempid :db.part/user)
        tx [{:db/id tempid
             :comment/text text
             :_comments tid}]
        res @(d/transact conn tx)
        db (:db-after res)
        cid (d/resolve-tempid db (:tempids res) tempid)]
    [cid db]))

(defn remove-comment [cid]
  [:db.fn/retractEntity cid])

;; mapping

(defn entity->map [source selection]
  (->>
    source
    (map (fn [[from v]]
           (let [[to mapping] (from selection)]
             (cond
               (nil? to) [nil nil] ;; will be filtered out later
               (nil? mapping) [to v] ;; just remap key
               (fn? mapping) [to (mapping v)])) ;; use mapping function
           ))
    (filter #(not= [nil nil] %))
    (into {})))
