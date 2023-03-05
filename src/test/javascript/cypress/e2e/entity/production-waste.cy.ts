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

describe('ProductionWaste e2e test', () => {
  const productionWastePageUrl = '/production-waste';
  const productionWastePageUrlPattern = new RegExp('/production-waste(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productionWasteSample = {};

  let productionWaste;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/production-wastes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/production-wastes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/production-wastes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productionWaste) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/production-wastes/${productionWaste.id}`,
      }).then(() => {
        productionWaste = undefined;
      });
    }
  });

  it('ProductionWastes menu should load ProductionWastes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('production-waste');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductionWaste').should('exist');
    cy.url().should('match', productionWastePageUrlPattern);
  });

  describe('ProductionWaste page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productionWastePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductionWaste page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/production-waste/new$'));
        cy.getEntityCreateUpdateHeading('ProductionWaste');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productionWastePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/production-wastes',
          body: productionWasteSample,
        }).then(({ body }) => {
          productionWaste = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/production-wastes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/production-wastes?page=0&size=20>; rel="last",<http://localhost/api/production-wastes?page=0&size=20>; rel="first"',
              },
              body: [productionWaste],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productionWastePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductionWaste page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productionWaste');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productionWastePageUrlPattern);
      });

      it('edit button click should load edit ProductionWaste page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductionWaste');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productionWastePageUrlPattern);
      });

      it.skip('edit button click should load edit ProductionWaste page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductionWaste');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productionWastePageUrlPattern);
      });

      it('last delete button click should delete instance of ProductionWaste', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productionWaste').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', productionWastePageUrlPattern);

        productionWaste = undefined;
      });
    });
  });

  describe('new ProductionWaste page', () => {
    beforeEach(() => {
      cy.visit(`${productionWastePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductionWaste');
    });

    it('should create an instance of ProductionWaste', () => {
      cy.get(`[data-cy="material"]`).type('Dakota Kentucky').should('have.value', 'Dakota Kentucky');

      cy.get(`[data-cy="quantity"]`).type('29956').should('have.value', '29956');

      cy.get(`[data-cy="transportType"]`).type('Central').should('have.value', 'Central');

      cy.get(`[data-cy="electric"]`).type('67176').should('have.value', '67176');

      cy.get(`[data-cy="water"]`).type('54964').should('have.value', '54964');

      cy.get(`[data-cy="waste"]`).type('Savings Fish Granite').should('have.value', 'Savings Fish Granite');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        productionWaste = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', productionWastePageUrlPattern);
    });
  });
});
