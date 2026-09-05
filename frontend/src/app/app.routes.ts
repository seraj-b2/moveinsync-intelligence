import { Routes } from '@angular/router';
import { VendorDashboardComponent } from './vendors/vendor-dashboard';

export const routes: Routes = [
  { path: 'vendors', component: VendorDashboardComponent },
  { path: '', redirectTo: 'vendors', pathMatch: 'full' }
];
