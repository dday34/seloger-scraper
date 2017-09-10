(ns seloger-scraper.core
  (:require
   [clojure.string :as string]
   [clojure.tools.cli :refer [parse-opts]]
   [clojure.tools.logging :as log]
   [net.cgrand.enlive-html :as html]
   [org.httpkit.client :as http]
   [sparkledriver.core :refer [with-browser make-browser close-browser!]]
   [seloger-scraper.listing :as listing])
  (:gen-class))

(def browser (make-browser))

(defn fetch-url [url]
  @(http/get url {:as :stream}))

(defn get-link [l]
  (-> l :attrs :href))

(defn get-number-of-pages [url]
  (let [pagination-element (html/select
                            (html/html-resource (:body (fetch-url url)))
                            [:p.pagination_result_number])]
      (if-not (empty? pagination-element)
        (->> pagination-element
             first
             :content
             first
             (re-seq #"[0-9]+")
             second
             Integer.)
        1)))

(defn get-listing-links [url]
  (map get-link (html/select (html/html-resource (:body (fetch-url url))) [:a.c-pa-link])))

(defn scrape-seloger-listings [url output-file-name]
  (let [listing-links (get-listing-links url)]
    (log/info "Start scraping" (count listing-links) "listings")
    (doseq [listing-link listing-links]
      (listing/scrape browser output-file-name listing-link))))

(def cities {"montpellier" "340172"})

(defn get-city-id [city]
  (let [city-ids cities]
      (get city-ids city)))

(defn construct-seloger-url
  ([city max-price] (construct-seloger-url city max-price 1))
  ([city max-price page]
   (str "http://www.seloger.com/list.htm?tri=initial&idtypebien=2,1&idtt=2&ci=" (get-city-id city) "&pxmax=" max-price "&naturebien=1,2,4&LISTING-LISTpg=" page)))

(defn search [city max-price]
  (log/info "Scrape listings for" city "with maximum price" max-price "...")
  (let [output-file-name (str (.format (java.text.SimpleDateFormat. "ddMMyyyy") (new java.util.Date)) "-" city "-" max-price "max.json")
        url (construct-seloger-url city max-price)]
    (doseq [page (range 1 (inc (get-number-of-pages url)))]
      (scrape-seloger-listings (construct-seloger-url city max-price page) output-file-name))))

(def cli-options
  [["-c" "--city" "Name of the city you want to search listings for."
    :required true
    :default "montpellier"
    :parse-fn str
    :validate [#(get-city-id %) (str "Must be one of these cities: " (string/join "," (keys cities)))]]
   ["-m" "--max-price" "The Maximum price for the listings."
    :required true
    :default 50000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 %) "Must be greater than zero."]]])

(defn -main
  [& args]
  (let [{{city :city max-price :max-price} :options errors :errors} (parse-opts args cli-options)]
    (if errors
      (log/info errors)
      (search city max-price))))
