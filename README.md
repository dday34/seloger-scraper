# seloger-scraper

Scrape accomodation listings on seloger.com

## Installation

`lein uberjar`

## Usage

    $ java -jar seloger-scraper-0.1.0-standalone.jar [args]

## Options

```
-c --city Name of the city you want to search accommodation in.
-m --max-price The Maximum selling price.
-h --help
```

## Examples

`java -jar ./target/uberjar/seloger-scraper-0.1.0-SNAPSHOT-standalone.jar -c montpellier -m 10000`

## Todo

- [ ] Find city id based on name (instead of hardcoded list)
- [ ] Add apartment type
- [ ] Add more information for each listing
- [ ] Improve scraping speed

