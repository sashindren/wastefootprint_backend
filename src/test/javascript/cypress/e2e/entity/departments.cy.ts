import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Departments e2e test', () => {
  const departmentsPageUrl = '/departments';
  const departmentsPageUrlPattern = new RegExp('/departments(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const departmentsSample = {};

  let departments;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/departments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/departments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/departments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (departments) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/departments/${departments.id}`,
      }).then(() => {
        departments = undefined;
      });
    }
  });

  it('Departments menu should load Departments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('departments');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Departments').should('exist');
    cy.url().should('match', departmentsPageUrlPattern);
  });

  describe('Departments page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(departmentsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Departments page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/departments/new$'));
        cy.getEntityCreateUpdateHeading('Departments');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/departments',
          body: departmentsSample,
        }).then(({ body }) => {
          departments = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/departments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/departments?page=0&size=20>; rel="last",<http://localhost/api/departments?page=0&size=20>; rel="first"',
              },
              body: [departments],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(departmentsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Departments page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('departments');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentsPageUrlPattern);
      });

      it('edit button click should load edit Departments page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Departments');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentsPageUrlPattern);
      });

      it.skip('edit button click should load edit Departments page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Departments');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentsPageUrlPattern);
      });

      it('last delete button click should delete instance of Departments', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('departments').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentsPageUrlPattern);

        departments = undefined;
      });
    });
  });

  describe('new Departments page', () => {
    beforeEach(() => {
      cy.visit(`${departmentsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Departments');
    });

    it('should create an instance of Departments', () => {
      cy.get(`[data-cy="departmentName"]`).type('RAM').should('have.value', 'RAM');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        departments = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', departmentsPageUrlPattern);
    });
  });
});
