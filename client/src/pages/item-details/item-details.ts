import { Component } from '@angular/core';

import { NavController, NavParams } from 'ionic-angular';
import { ListPage } from '../list/list';

@Component({
  selector: 'page-item-details',
  templateUrl: 'item-details.html'
})
export class ItemDetailsPage {
  selectedItem: {listing_id: number};

  constructor(public navCtrl: NavController, public navParams: NavParams) {
    // If we navigated to this page, we will have an item available as a nav param
    this.selectedItem = navParams.get('item');
  }

  navigateHome() {
    this.navCtrl.setRoot(ListPage, {
      categoryPath: []
    });
  }
}
