import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { Category } from './categories-service';

import _ from 'lodash';

const LUCENE_SIMILARITY_URL = 'http://localhost/get-similar-listings';

const REAL_ENDPOINT = 'https://openapi.etsy.com/v2';
const PROXY_ENDPOINT = 'http://localhost:8100/etsy';
const ETSY_API_KEY = '';
const FAT_LISTING_FIELDS = [
  'listing_id', 'state', 'user_id', 'title', 'creation_tsz', 'ending_tsz',
  'tags', 'category_path', 'category_path_ids', 'materials', 'views', 'num_favorers', 'is_supply',
  'occasion', 'style', 'has_variations', 'taxonomy_path', 'taxonomy_id', 'url', 'description'
];

const MOCK = {
  "listing_id": 116315317,
  "state": "active",
  "user_id": 17544963,
  "title": "Noritake China Goldston pattern vintage retired bread plate mid century modern design",
  "creation_tsz": 1484614824,
  "ending_tsz": 1494979224,
  "tags": ["mid century modern", "cannibas", "Noritake", "vintage", "bone china", "gold leaf", "hand painted", "retired pattern", "Goldston", "plate", "vl team", "midmodteam"],
  "category_path": ["Vintage", "Serving", "Plate"],
  "category_path_ids": [69150437, 69154629, 68906258],
  "materials": ["bone chine", "gold", "paint"],
  "views": 64,
  "num_favorers": 2,
  "is_supply": "false",
  "occasion": null,
  "style": ["Mid Century", "Modern"],
  "has_variations": false,
  "suggested_taxonomy_id": 1049,
  "taxonomy_path": ["Home & Living", "Kitchen & Dining", "Dining & Serving", "Plates"],
  "used_manufacturer": false,
  "main_image_url": "https://img0.etsystatic.com/007/0/6605988/il_570xN.400657028_p2rv.jpg",
  "url": "https://www.etsy.com/listing/116315317",
  "description": "Noritake mid century fine china Goldston pattern, vintage retired hand painted and gold leaf bread plate. In excellent condition. Price is by the piece. Please check out other pieces in this shop. Purchase as many as you want, if you need combined shipping please contact me. This pattern reminds me of Cannibas. It works very well with mid century modern, hollywood regency and classic designs. 6 1/4&quot;"
 };

export interface SlimListing {
  listingId: number,
  title: string,
  categoryPathIds: Array<number>
}

export interface FatListing {
  listing_id: number,
  state: string,
  user_id: number,
  title: string,
  creation_tsz: number,
  ending_tsz: number,
  tags: Array<string>,
  category_path: Array<string>,
  category_path_ids: Array<number>,
  materials: Array<string>,
  views: number,
  num_favorers: number,
  is_supply: boolean,
  occasion: string,
  style: Array<string>,
  has_variations: boolean,
  suggested_taxonomy_id: number,
  taxonomy_path: Array<string>,
  used_manufacturer: boolean,
  // Fields below shold not be send to Lucene.
  main_image_url: string,
  url: string,
  description: string
}

@Injectable()
export class ListingsService {

  private localListings: Array<SlimListing>;
  private initialized: boolean;

  constructor(public http: Http) {
    this.localListings = [];
    this.initialized = false;
  }

  private init(callback) {
    this.http.get('assets/json/clientListingDBSingleLine.json')
      .map(res => res.json())
      .subscribe(data => {
        this.localListings = data;
        this.initialized = true;
        callback();
      });
  }

  private asserInitialized() {
    return new Promise((resolve) => {
      if (this.initialized) {
        resolve();
      } else {
        this.init(resolve);
      }
    });
  }

  private findListingsForCategoryPath(categoryPath: Array<Category>) {
    if (categoryPath.length === 0) {
      return [];
    }
    let level1 = _.get(categoryPath, [0, 'category_id'], -1);
    let level2 = _.get(categoryPath, [1, 'category_id'], -1);
    let level3 = _.get(categoryPath, [2, 'category_id'], -1);

    return this.localListings.filter((listing) => {
      let listingCat1 = _.get(listing.categoryPathIds, 0, -1);
      let listingCat2 = _.get(listing.categoryPathIds, 1, -1);
      let listingCat3 = _.get(listing.categoryPathIds, 2, -1);

      let match = listingCat1 === level1;
      if (level2 !== -1) {
        match = match && listingCat2 === level2;
      }
      if (level3 !== -1) {
        match = match && listingCat3 === level3;
      }

      return match;
    });
  }

  getListingsForCategoryPath(categoryPath: Array<Category>, begin: number, count: number) {
    return this.asserInitialized()
      .then(() => {
        let listingsForCategory: Array<SlimListing> = this.findListingsForCategoryPath(categoryPath);
        return listingsForCategory.slice(begin, begin + count);
      });
  }

  getNumberOfListingsForCategoryPath(categoryPath: Array<Category>) {
    return this.asserInitialized()
      .then(() => {
        return this.findListingsForCategoryPath(categoryPath).length;
      });
  }

  private _getListingDetails(listingId: number, successCB, errorCB) {
    let params = '?';
    params+='api_key=' + ETSY_API_KEY;
    params+='&fields=' + FAT_LISTING_FIELDS.join(',');
    params+='&includes=MainImage';

    var request = this.http.get(PROXY_ENDPOINT + '/listings/' + listingId + params)
    .catch((err) => {
      return this.http.get(REAL_ENDPOINT + '/listings/' + listingId + params)
    })
    .map(res => res.json())
    .subscribe((data) => {
      let itemData = data.results[0];
      let itemDetails : FatListing = _.merge(_.omit(itemData, 'MainImage'), { main_image_url: itemData.MainImage.url_570xN });
      successCB(itemDetails);
    }, errorCB);
  }

  getListingDetails(listingId: number) : Promise<FatListing> {
    return Promise.resolve(MOCK);
    /*
      return new Promise((resolve, reject) => {
        this._getListingDetails(listingId, resolve, () => {
          resolve(MOCK);
        });
      });
  */
  }

  getSimilarItems(listing: FatListing) : Promise<Array<SlimListing>> {
    let data = _.omit(listing, ['main_image_url', 'url', 'description']);
    return new Promise((resolve, reject) => {
      this.http.post(LUCENE_SIMILARITY_URL, data)
      .map(res => res.json())
      .subscribe((data) => {
        resolve(data);
      },
      reject);
    });
  }
}
