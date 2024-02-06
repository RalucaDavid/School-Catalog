import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { MyCoursesComponent } from './components/my-courses/my-courses.component';
import { AdminComponent } from './components/admin/admin.component';
import { LoggedGuard } from './guards/logged.guard';
import { SuperUserGuard } from './guards/super-user.guard';
import { SubjectStudentsComponent } from './components/subject-students/subject-students.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'my-courses',
    component: MyCoursesComponent,
    canActivate: [LoggedGuard]
  },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [SuperUserGuard]
  },
  {
    path: 'subject/:id',
    component: SubjectStudentsComponent,
    canActivate: [LoggedGuard],
  },
  {
    path: '**',
    redirectTo: 'my-courses'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
