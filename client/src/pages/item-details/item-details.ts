import { Component } from '@angular/core';

import { NavController, NavParams } from 'ionic-angular';
import { ListPage } from '../list/list';
import { SlimListing, FatListing, ListingsService } from '../../providers/listings-service';
import _ from 'lodash';

@Component({
  selector: 'page-item-details',
  templateUrl: 'item-details.html'
})
export class ItemDetailsPage {
  selectedItem: SlimListing;
  itemDetails: FatListing;
  similarItems: Array<SlimListing>;
  buttonSimilarEnabled: boolean;

  constructor(public navCtrl: NavController, public navParams: NavParams, public listingsService: ListingsService) {
    this.selectedItem = navParams.get('item');
    this.itemDetails = null;
    this.similarItems = [];
    this._loadDetails();
    this.buttonSimilarEnabled = true;
  }

  private _loadDetails() {
    this.listingsService
      .getListingDetails(this.selectedItem.listingId)
      .then((res) => {
        this.itemDetails = res;
      });
  }

  navigateHome() {
    this.navCtrl.setRoot(ListPage, {
      categoryPath: []
    });
  }

  goToDetails(item) {
    this.navCtrl.push(ItemDetailsPage, {
      item: item
    });
  }

  getSimilar() {
    this.buttonSimilarEnabled = false;
    this.listingsService
      .getSimilarItems(this.itemDetails)
      .then((res) => {
        this.similarItems = res;
        this.buttonSimilarEnabled = true;
         _.remove(this.similarItems, item => item.listingId === this.itemDetails.listing_id);
      }, (err) => {
        // Mock response on error
        this.listingsService.getListingsForCategoryPath([{
          "category_id": 69150467,
          "name": "accessories",
          "meta_title": "Handmade Accessories on Etsy - Belts, hats, pins, scarves",
          "meta_keywords": "handmade accessories, handmade belt, handmade hat, handmade wallet, handmade scarf, handmade keychain, handmade necktie, handmade accessory",
          "page_title": "Handmade accessories",
          "short_name": "Accessories",
          "num_children": 27,
          "level": 1,
          "parent_id": null,
          "grandparent_id": null
        }], 0, 11).then((res) => {
          this.similarItems = res;
          this.buttonSimilarEnabled = true;
        });
      });
  }
}
