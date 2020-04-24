import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppsComponent } from './apps.component';

describe('AppsComponent', () => {
  let component: AppsComponent;
  let fixture: ComponentFixture<AppsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    fixture.debugElement.nativeElement.style.visibility = "hidden";
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
