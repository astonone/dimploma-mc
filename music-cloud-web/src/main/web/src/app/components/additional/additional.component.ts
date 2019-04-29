import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-additional',
  templateUrl: './additional.component.html',
  styleUrls: ['./additional.component.css']
})
export class AdditionalComponent implements OnInit {

  constructor(private router: Router,
              private route: ActivatedRoute) {
    const trackId = this.route.snapshot.paramMap.get('id');
  }

  ngOnInit() {
  }

}
