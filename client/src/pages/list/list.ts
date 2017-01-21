import { Component } from '@angular/core';

import { NavController, NavParams } from 'ionic-angular';

import { ItemDetailsPage } from '../item-details/item-details';


@Component({
  selector: 'page-list',
  templateUrl: 'list.html'
})
export class ListPage {
  icons: string[];
  items: Array<{listing_id: number}>;

  constructor(public navCtrl: NavController, public navParams: NavParams) {
    this.items = [];
    for(let i = 1; i < 11; i++) {
      this.items.push({
        listing_id: i*123 + i*5 + i%13 + i,
      });
    }
  }

  itemTapped(event, item) {
    this.navCtrl.push(ItemDetailsPage, {
      item: item
    });
  }
}
