package facade;

import controller.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import model.Disciplina;
import model.Disponibilidade;
import model.Pessoa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;


@Stateless
public class DisponibilidadeFacade extends AbstractFacade<Disponibilidade>{
    
    public DisponibilidadeFacade() {
        super(Disponibilidade.class);
    }

    @Override
    protected SessionFactory getSessionFactory() {

        return HibernateUtil.getSessionFactory();

    }
    
    
    public Disponibilidade findByIDs(Long turmaId, Long pessoaId){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("turmaId", turmaId));
            criteria.add(Restrictions.eq("pessoaId", pessoaId));
            List resultado = criteria.list();
            

            if (resultado.size() <= 0) {
                session.close();
                return null;
            } else {
                Disponibilidade d = (Disponibilidade) resultado.get(0);
                session.close();
                return d;
            }
        } catch (HibernateException e) {
            return null;
        }
        
        
        
    }
    
    public List<Disponibilidade> findByPessoa(Pessoa pessoa){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("pessoa", pessoa));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List resultado = criteria.list();
            
            session.close();
            return resultado;
            
        } catch (HibernateException e) {
            return null;
        }
     
    }
    
    public List<Disponibilidade> findByPessoaQuad(Pessoa pessoa, int quad){
        
       try {

            Session session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Disponibilidade.class);
            criteria.add(Restrictions.eq("pessoa", pessoa));
            if(quad != 0){
                 criteria.createAlias("ofertaDisciplina", "t").add(Restrictions.eq("t.quadrimestre", quad));
            }
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List resultado = criteria.list();
            
            session.close();
            return resultado;
            
        } catch (HibernateException e) {
            return null;
        }
     
    }
    
    //Retorna a lista de disponibilidade de acordo com a disciplina pesquisada
    //Turno e campus são atributos opcionais
    public List<Disponibilidade> findByDisciplinaTC(Disciplina d, String campus, String turno){
        
        try{
            
            Session session = getSessionFactory().openSession();
            Criteria crit = session.createCriteria(Disponibilidade.class);
            
            if(d != null){
                
                crit.createAlias("turma", "t").add(Restrictions.eq("t.disciplina", d));
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
                
                
            }
            else{
                
                crit.createAlias("turma", "t");
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
            }
            
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List result = crit.list();
            return result;
            
        }
        catch(HibernateException e){
            return null;
        }

        
    }
    
    //Retorna a lista de disponibilidade de acordo com a disciplina pesquisada
    //Turno, campus e quadrimestre são atributos opcionais
    public List<Disponibilidade> findByDisciplinaTCQ(Disciplina d, String campus, String turno, int quadrimestre){
        
        try{
            
            Session session = getSessionFactory().openSession();
            Criteria crit = session.createCriteria(Disponibilidade.class);
            
            if(d != null){
                
                if(quadrimestre != 0){
                    crit.add(Restrictions.eq("quadrimestre", quadrimestre));
                }
                
                crit.createAlias("turma", "t").add(Restrictions.eq("t.disciplina", d));
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
                
                
                
                
            }
            else{
                
                if(quadrimestre != 0){
                    crit.add(Restrictions.eq("quadrimestre", quadrimestre));
                }
                
                crit.createAlias("turma", "t");
                
                if(!campus.equals("")){
                    crit.add(Restrictions.eq("t.campus", campus));
                }
                
                if(!turno.equals("")){
                    crit.add(Restrictions.eq("t.turno", turno));
                }
            }
            
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List result = crit.list();
            return result;
            
        }
        catch(HibernateException e){
            return null;
        }

        
    }
    
    public List<Disponibilidade> findByAreaQuad(List<String> areasAtuacao, int quad){
        
       try {

            Session session = getSessionFactory().openSession();
           Criteria criteria = session.createCriteria(Disponibilidade.class);
           List<Disponibilidade> disponibilidades = new ArrayList<>();

           if (quad != 0) {

               if (areasAtuacao != null) {

                   for (String a : areasAtuacao) {

                       criteria.createAlias("ofertaDisciplina", "o").add(Restrictions.eq("o.quadrimestre", quad));
                       criteria.createAlias("pessoa", "p").add(Restrictions.eq("p.areaAtuacao", a));
                       criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                       List resultado = criteria.list();
                       disponibilidades.addAll(resultado);
                       criteria = session.createCriteria(Disponibilidade.class);
                   }

               } else {
                   criteria.createAlias("ofertaDisciplina", "o").add(Restrictions.eq("o.quadrimestre", quad));
                   List resultado = criteria.list();
                   disponibilidades.addAll(resultado);
                   criteria = session.createCriteria(Disponibilidade.class);
               }

           }

            session.close();
            return disponibilidades;
            
        } catch (HibernateException e) {
            return null;
        }
     
    }
    
  
    
    
    
}
