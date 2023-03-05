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

describe('EmployeeDetails e2e test', () => {
  const employeeDetailsPageUrl = '/employee-details';
  const employeeDetailsPageUrlPattern = new RegExp('/employee-details(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const employeeDetailsSample = {};

  let employeeDetails;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/employee-details+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/employee-details').as('postEntityRequest');
    cy.intercept('DELETE', '/api/employee-details/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (employeeDetails) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/employee-details/${employeeDetails.id}`,
      }).then(() => {
        employeeDetails = undefined;
      });
    }
  });

  it('EmployeeDetails menu should load EmployeeDetails page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('employee-details');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EmployeeDetails').should('exist');
    cy.url().should('match', employeeDetailsPageUrlPattern);
  });

  describe('EmployeeDetails page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(employeeDetailsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EmployeeDetails page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/employee-details/new$'));
        cy.getEntityCreateUpdateHeading('EmployeeDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeDetailsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/employee-details',
          body: employeeDetailsSample,
        }).then(({ body }) => {
          employeeDetails = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/employee-details+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/employee-details?page=0&size=20>; rel="last",<http://localhost/api/employee-details?page=0&size=20>; rel="first"',
              },
              body: [employeeDetails],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(employeeDetailsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EmployeeDetails page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('employeeDetails');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeDetailsPageUrlPattern);
      });

      it('edit button click should load edit EmployeeDetails page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeDetails');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeDetailsPageUrlPattern);
      });

      it.skip('edit button click should load edit EmployeeDetails page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EmployeeDetails');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeDetailsPageUrlPattern);
      });

      it('last delete button click should delete instance of EmployeeDetails', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('employeeDetails').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', employeeDetailsPageUrlPattern);

        employeeDetails = undefined;
      });
    });
  });

  describe('new EmployeeDetails page', () => {
    beforeEach(() => {
      cy.visit(`${employeeDetailsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EmployeeDetails');
    });

    it('should create an instance of EmployeeDetails', () => {
      cy.get(`[data-cy="employeeName"]`).type('National').should('have.value', 'National');

      cy.get(`[data-cy="address"]`).type('Connecticut panel').should('have.value', 'Connecticut panel');

      cy.get(`[data-cy="phoneNumber"]`).type('34355').should('have.value', '34355');

      cy.get(`[data-cy="homeNumber"]`).type('39012').should('have.value', '39012');

      cy.get(`[data-cy="emailAddress"]`).type('International teal').should('have.value', 'International teal');

      cy.get(`[data-cy="transportType"]`).type('Inverse').should('have.value', 'Inverse');

      cy.get(`[data-cy="jobTitle"]`).type('Direct Accounts Specialist').should('have.value', 'Direct Accounts Specialist');

      cy.get(`[data-cy="supervisorName"]`).type('Personal compress overriding').should('have.value', 'Personal compress overriding');

      cy.get(`[data-cy="companyId"]`).type('9821').should('have.value', '9821');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        employeeDetails = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', employeeDetailsPageUrlPattern);
    });
  });
});
