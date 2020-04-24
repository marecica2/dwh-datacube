import { TestBed } from "@angular/core/testing";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { TestbedHarnessEnvironment } from "@angular/cdk/testing/testbed";

import { AngularMaterialModule } from "../shared/angular-material.module";
import { HarnessLoader } from "@angular/cdk/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { routes } from "../auth/auth.module";

export async function setupMaterialComponentTest({ module, imports = [], declarations = [], providers = [] }) {
  await TestBed.configureTestingModule({
    imports: [
      AngularMaterialModule,
      NoopAnimationsModule,
      RouterTestingModule.withRoutes(routes),
      ...imports,
    ],
    declarations: [
      module,
      ...declarations,
    ],
    providers: [
      ...providers,
    ],
  })
    .compileComponents();

  const fixture = TestBed.createComponent(module);
  const loader: HarnessLoader = TestbedHarnessEnvironment.loader(fixture);
  const rootLoader = TestbedHarnessEnvironment.documentRootLoader(fixture);
  return { fixture, loader, rootLoader };
}
