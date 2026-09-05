import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { VendorDashboardComponent } from './vendors/vendor-dashboard';
import { ManagerDashboardComponent } from './manager/manager-dashboard';
import { EmployeeDashboardComponent } from './employee/employee-dashboard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'vendors', component: VendorDashboardComponent },
  { path: 'manager/:companyName/:managerId', component: ManagerDashboardComponent },
  { path: 'manager', redirectTo: 'manager/catalyst-Sac/MGR-103', pathMatch: 'full' },
  { path: 'employee/:companyName/:employeeId', component: EmployeeDashboardComponent },
  { path: 'employee/:employeeId', component: EmployeeDashboardComponent },
  { path: 'employee', redirectTo: 'employee/catalyst-Sac/STW-484475', pathMatch: 'full' },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
