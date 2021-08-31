package Tests;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.junit.Test;



public class TestsConsultaCEP {

    /**
     1 - Scenario: Consulta CEP valido
     Given   que o usuário inseri um CEP válido
     When    o serviço é consultado
     Then    é retornado o CEP, logradouro, complemento, bairro, localidade, uf e ibge

     */

    @Test
    public void deveVerificarConsultaCEPValido(){

        given()
        .when()
            .get("https://viacep.com.br/ws/01001000/json/")
        .then()
            .statusCode(200)
                .body("cep", is("01001-000"))
                .body("logradouro", is("Praça da Sé"))
                .body("complemento", is("lado ímpar"))
                .body("bairro", is("Sé"))
                .body("localidade", is("São Paulo"))
                .body("uf", is("SP"))
                .body("ibge", is("3550308"))
        ;

        /**
         Tambem pode ser feito neste formato usando response, JsonPath e Assert
         Creio que tenham formatos melhores usando array e loops para verificar, mas ai é pensando em manutenção do código e reaproveitamento.

         Response response = request(Method.GET, "https://viacep.com.br/ws/01001000/json/");
         assertEquals(200, response.statusCode());
         JsonPath jpath = new JsonPath(response.asString());
         assertEquals("01001-000", jpath.getString("cep"));
         assertEquals("Praça da Sé", jpath.getString("logradouro"));
         assertEquals("lado ímpar", jpath.getString("complemento"));
         assertEquals("Sé", jpath.getString("bairro"));
         assertEquals("São Paulo", jpath.getString("localidade"));
         assertEquals("SP", jpath.getString("uf"));
         assertEquals("3550308", jpath.getString("ibge"));
         */

        /**
         PS: Tambem é possivel integrar com cucumber caso necessário. Neste caso ajuda na passagem da massa dos testes e documentação da história de usuário
         tambem para fazer funções mais genéricas com parametros na URL e resposta, mas como minha especialidade é web,
         não arrisquei a nova estrutura e fiz o básico fixo para o Rest Assured.
         */
        }

    /**
     2 - Scenario: Consulta CEP inexistente
     Given   que o usuário inseri um CEP que não exista na base dos Correios
     When    o serviço é consultado
     Then    é retornada um atributo erro

     */


    @Test
    public void deveVerificarConsultaCEPInexistente(){

        given()
        .when()
            .get("https://viacep.com.br/ws/99999999/json/")
        .then()
            .statusCode(200)
            .body("erro", is(true))
        ;

        /**
         .statusCode(404) geralmente é usado para retorno de itens inexistentes

         Tambem pode ser feito neste formato usando  validatable response e assert

         Response response = request(Method.GET, "https://viacep.com.br/ws/99999999/json/");
         ValidatableResponse validacao = response.then();
         validacao.statusCode(200);
         assertTrue("erro", true);
         assertTrue("true", true);

         */
    }

    /**
     3 - Scenario: Consulta CEP com formato inválido
     Given   que o usuário inseri um CEP com formato inválido
     When    o serviço é consultado
     Then    é retornado uma mensagem de erro

     */

    @Test
    public void deveVerificarConsultaCEPFormatoInvalido(){

        given()
        .when()
            .get("https://viacep.com.br/ws/95010A10/json/")
        .then()
             .statusCode(400)
             .body("html.body.h1", is("Erro 400"))
             .body("html.body.h2", is("Ops!"))
             .body("html.body.h3", is("Verifique a sua URL (Bad Request)"))
        ;

    }




    }

