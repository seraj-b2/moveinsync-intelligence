import { Routes } from '@angular/router';
import { VendorDashboardComponent } from './vendors/vendor-dashboard';

export const routes: Routes = [
  { path: 'vendor-operations', component: VendorDashboardComponent },
  { path: '', redirectTo: 'vendor-operations', pathMatch: 'full' }
];
